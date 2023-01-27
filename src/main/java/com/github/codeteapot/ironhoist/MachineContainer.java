package com.github.codeteapot.ironhoist;

import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.github.codeteapot.ironhoist.event.MachineAvailableEvent;
import com.github.codeteapot.ironhoist.event.MachineLostEvent;
import com.github.codeteapot.ironhoist.machine.MachineContext;
import com.github.codeteapot.ironhoist.machine.MachineRef;
import com.github.codeteapot.ironhoist.machine.UnknownUserException;
import com.github.codeteapot.ironhoist.port.MachineLink;
import com.github.codeteapot.ironhoist.session.MachineSessionFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import java.util.stream.Stream;

class MachineContainer {

  private static final Logger logger = getLogger(MachineContainer.class.getName());

  private final PlatformEventSource eventSource;
  private final MachineCatalog catalog;
  private final MachineSessionFactory sessionFactory;
  private final Executor actionExecutor;
  private final ScheduledExecutorService sessionPoolReleaseExecutor;
  private final MachineReadyChecker readyChecker;
  private final Map<MachineRef, ManagedMachine> machineMap;
  private final Map<MachineRef, Lock> actionLockMap;

  MachineContainer(
      PlatformEventSource eventSource,
      MachineCatalog catalog,
      MachineSessionFactory sessionFactory,
      Executor actionExecutor) {
    this.eventSource = requireNonNull(eventSource);
    this.catalog = requireNonNull(catalog);
    this.sessionFactory = requireNonNull(sessionFactory);
    this.actionExecutor = requireNonNull(actionExecutor);
    sessionPoolReleaseExecutor = newSingleThreadScheduledExecutor();
    readyChecker = new MachineReadyChecker();
    machineMap = new HashMap<>();
    actionLockMap = new ConcurrentHashMap<>();
  }

  PlatformContext getContext() {
    return new PlatformContextImpl(this::availableSupplier, this::machineMapper);
  }

  void take(MachineRef ref, MachineLink link) {
    actionExecutor.execute(() -> {
      Lock actionLock = actionLockMap.computeIfAbsent(ref, this::newLock);
      try {
        actionLock.lock();
        if (machineMap.containsKey(ref)) {
          logger.warning(new StringBuilder()
              .append("Machine ").append(ref).append(" already exists")
              .toString());
        } else {
          link.getProfileName()
              .flatMap(catalog::getProfile)
              .map(this::withProfile)
              .orElseGet(this::withoutProfile)
              .take(ref, link);
        }
      } catch (InterruptedException e) {
        currentThread().interrupt();
      } finally {
        actionLockMap.remove(ref);
        actionLock.unlock();
      }
    });
  }

  void forget(MachineRef ref) {
    actionExecutor.execute(() -> {
      Lock actionLock = actionLockMap.computeIfAbsent(ref, this::newLock);
      try {
        actionLock.lock();
        if (machineMap.containsKey(ref)) {
          machineMap.remove(ref).dispose();
          eventSource.fireEvent(new MachineLostEvent(this, ref));
        } else {
          logger.warning(new StringBuilder()
              .append("Machine ").append(ref).append(" does not exist")
              .toString());
        }
      } finally {
        actionLockMap.remove(ref);
        actionLock.unlock();
      }
    });
  }

  private TakeMachineClause withProfile(MachineProfile profile) {
    return (ref, link) -> {
      try {
        PooledMachineSessionFactory pooledSessionFactory = new PooledMachineSessionFactory(
            sessionFactory,
            sessionPoolReleaseExecutor,
            ref,
            profile.getSessionPool());
        MachineContext context = new MachineContextImpl(
            ref,
            profile.getRealm(),
            link,
            profile.getNetworkName(),
            profile.getSessionPort().orElse(null),
            pooledSessionFactory);
        readyChecker.awaitReady(context, profile.getReadyCheck());
        ManagedMachineFactory factory = new ManagedMachineFactoryImpl(context);
        factory.build(profile.getBuilder());
        machineMap.put(ref, factory.getMachine(pooledSessionFactory));
        eventSource.fireEvent(new MachineAvailableEvent(this, ref));
      } catch (UnknownUserException e) {
        logger.log(SEVERE, "Unable to check machine is ready", e);
      } catch (MachineBuildingException | RuntimeException e) {
        logger.log(SEVERE, "Unable to take machine", e);
      }
    };
  }

  private TakeMachineClause withoutProfile() {
    return (ref, link) -> logger.warning(new StringBuilder()
        .append("Could not determine profile for machine ").append(ref)
        .toString());
  }

  private Stream<? extends Machine> availableSupplier() {
    return machineMap.values().stream();
  }

  private Optional<? extends Machine> machineMapper(MachineRef ref) {
    return ofNullable(machineMap.get(ref));
  }

  private Lock newLock(MachineRef ref) {
    return new ReentrantLock();
  }
}

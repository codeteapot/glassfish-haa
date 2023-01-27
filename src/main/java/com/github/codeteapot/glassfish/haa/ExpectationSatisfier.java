package com.github.codeteapot.glassfish.haa;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.logging.Logger.getLogger;

import com.github.codeteapot.ironhoist.PlatformContext;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

class ExpectationSatisfier {

  private static final int EXECUTOR_CORE_POOL_SIZE = 1;

  private static final long SATISFY_ALL_INITIAL_DELAY_MILLIS = 0L;
  private static final long SATISFY_ALL_PERIOD_MILLIS = 2400L;

  private static final Logger logger = getLogger(ExpectationSatisfier.class.getName());

  private final PlatformContext context;
  private final Set<Expectation> expectations;
  private final AtomicBoolean active;
  private final ScheduledExecutorService executor;

  ExpectationSatisfier(PlatformContext context) {
    this.context = requireNonNull(context);
    expectations = new HashSet<>();
    active = new AtomicBoolean(true);
    executor = newScheduledThreadPool(EXECUTOR_CORE_POOL_SIZE);
    executor.scheduleAtFixedRate(
        this::satisfyAll,
        SATISFY_ALL_INITIAL_DELAY_MILLIS,
        SATISFY_ALL_PERIOD_MILLIS,
        MILLISECONDS);
  }

  synchronized void add(Expectation expectation) {
    logger.fine(new StringBuilder()
        .append("Adding expectation: ")
        .append(expectation)
        .toString());
    expectations.add(expectation);
  }

  void shutdown() {
    active.set(false);
    executor.shutdown();
  }

  private synchronized void satisfyAll() {
    if (active.get()) {
      expectations.removeIf(expectation -> expectation.satisfy(context));
    }
  }
}

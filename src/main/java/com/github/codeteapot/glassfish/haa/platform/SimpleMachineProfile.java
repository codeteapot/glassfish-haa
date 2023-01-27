package com.github.codeteapot.glassfish.haa.platform;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import com.github.codeteapot.ironhoist.MachineBuilder;
import com.github.codeteapot.ironhoist.MachineProfile;
import com.github.codeteapot.ironhoist.MachineReadyCheck;
import com.github.codeteapot.ironhoist.MachineRealm;
import com.github.codeteapot.ironhoist.MachineSessionPool;
import com.github.codeteapot.ironhoist.port.MachineNetworkName;
import java.util.Optional;

public class SimpleMachineProfile implements MachineProfile {

  private final MachineBuilderDeserializer builderDeserializer;
  private final MachineNetworkName networkName;
  private final Integer sessionPort;
  private final MachineRealm realm;
  private final MachineReadyCheck readyCheck;
  private final MachineSessionPool sessionPool;
  private final MachineBuilderFactory builderFactory;

  public SimpleMachineProfile(
      MachineBuilderDeserializer builderDeserializer,
      MachineNetworkName networkName,
      Integer sessionPort,
      MachineRealm realm,
      MachineReadyCheck readyCheck,
      MachineSessionPool sessionPool,
      MachineBuilderFactory builderFactory) {
    this.builderDeserializer = requireNonNull(builderDeserializer);
    this.networkName = requireNonNull(networkName);
    this.sessionPort = sessionPort;
    this.realm = requireNonNull(realm);
    this.readyCheck = requireNonNull(readyCheck);
    this.sessionPool = requireNonNull(sessionPool);
    this.builderFactory = requireNonNull(builderFactory);
  }

  @Override
  public MachineNetworkName getNetworkName() {
    return networkName;
  }

  @Override
  public Optional<Integer> getSessionPort() {
    return ofNullable(sessionPort);
  }

  @Override
  public MachineRealm getRealm() {
    return realm;
  }

  @Override
  public MachineReadyCheck getReadyCheck() {
    return readyCheck;
  }

  @Override
  public MachineSessionPool getSessionPool() {
    return sessionPool;
  }

  @Override
  public MachineBuilder getBuilder() {
    return builderFactory.getBuilder(builderDeserializer);
  }
}

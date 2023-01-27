package com.github.codeteapot.glassfish.haa.platform;

import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;

import com.github.codeteapot.ironhoist.MachineCatalog;
import com.github.codeteapot.ironhoist.MachineProfile;
import com.github.codeteapot.ironhoist.port.MachineProfileName;
import java.util.Map;
import java.util.Optional;

public class MapMachineCatalog implements MachineCatalog {

  private final Map<MachineProfileName, MachineProfile> profileMap;

  public MapMachineCatalog(Map<MachineProfileName, MachineProfile> profileMap) {
    this.profileMap = unmodifiableMap(profileMap);
  }

  @Override
  public Optional<MachineProfile> getProfile(MachineProfileName profileName) {
    return ofNullable(profileMap.get(profileName));
  }
}

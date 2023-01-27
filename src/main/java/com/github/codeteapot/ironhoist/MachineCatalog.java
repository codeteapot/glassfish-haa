package com.github.codeteapot.ironhoist;

import com.github.codeteapot.ironhoist.port.MachineProfileName;
import java.util.Optional;

public interface MachineCatalog {

  Optional<MachineProfile> getProfile(MachineProfileName profileName);
}

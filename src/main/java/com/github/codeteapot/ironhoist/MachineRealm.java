package com.github.codeteapot.ironhoist;

import com.github.codeteapot.ironhoist.session.MachineSessionAuthentication;
import java.util.Optional;

public interface MachineRealm {

  Optional<? extends MachineSessionAuthentication> authentication(String username);
}

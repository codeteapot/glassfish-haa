package com.github.codeteapot.ironhoist.port.docker;

import com.github.codeteapot.ironhoist.port.MachineProfileName;
import java.util.Optional;

@FunctionalInterface
public interface DockerRoleMapper {

  Optional<MachineProfileName> map(String roleName);
}

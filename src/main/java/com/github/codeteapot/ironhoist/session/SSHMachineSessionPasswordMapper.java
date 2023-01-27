package com.github.codeteapot.ironhoist.session;

import java.util.Optional;

@FunctionalInterface
interface SSHMachineSessionPasswordMapper {

  Optional<byte[]> map(MachineSessionPasswordName passwordName);
}

package com.github.codeteapot.ironhoist;

import com.github.codeteapot.ironhoist.machine.MachineRef;
import com.github.codeteapot.ironhoist.port.MachineLink;

@FunctionalInterface
interface TakeMachineClause {

  void take(MachineRef ref, MachineLink link) throws InterruptedException;
}

package com.github.codeteapot.ironhoist;

import com.github.codeteapot.ironhoist.session.MachineSession;

@FunctionalInterface
interface AcquireMachineSessionClause {

  MachineSession acquire(String username);
}

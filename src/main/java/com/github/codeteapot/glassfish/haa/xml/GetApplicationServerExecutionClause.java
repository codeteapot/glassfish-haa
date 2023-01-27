package com.github.codeteapot.glassfish.haa.xml;

import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerExecutionBean;
import com.github.codeteapot.ironhoist.MachineFacetInstantiationException;
import com.github.codeteapot.ironhoist.machine.MachineContext;

@FunctionalInterface
interface GetApplicationServerExecutionClause {

  ApplicationServerExecutionBean apply(MachineContext context)
      throws MachineFacetInstantiationException;
}

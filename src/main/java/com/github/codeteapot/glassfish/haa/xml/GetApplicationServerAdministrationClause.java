package com.github.codeteapot.glassfish.haa.xml;

import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerAdministrationBean;
import com.github.codeteapot.ironhoist.MachineFacetInstantiationException;
import com.github.codeteapot.ironhoist.machine.MachineContext;

@FunctionalInterface
interface GetApplicationServerAdministrationClause {

  ApplicationServerAdministrationBean apply(MachineContext context)
      throws MachineFacetInstantiationException;
}

package com.github.codeteapot.glassfish.haa;

import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationCluster;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationDomain;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerAdministration;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerExecution;

public interface Cluster {

  boolean managedBy(ApplicationServerAdministration administration);
  
  boolean managedBy(ApplicationServerExecution execution);

  boolean managedBy(ApplicationDomain domain);
  
  boolean managedBy(ApplicationCluster cluster);
}

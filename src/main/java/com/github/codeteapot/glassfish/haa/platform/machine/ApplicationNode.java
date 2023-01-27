package com.github.codeteapot.glassfish.haa.platform.machine;

import java.util.Set;

public interface ApplicationNode {

  ApplicationNodeName getName();

  Set<ApplicationInstance> getInstances();
}

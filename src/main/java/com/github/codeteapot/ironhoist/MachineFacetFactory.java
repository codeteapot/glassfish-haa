
package com.github.codeteapot.ironhoist;

import com.github.codeteapot.ironhoist.machine.MachineContext;
import com.github.codeteapot.ironhoist.machine.MachineFacet;

public interface MachineFacetFactory {

  MachineFacet getFacet(MachineContext context) throws MachineFacetInstantiationException;
}

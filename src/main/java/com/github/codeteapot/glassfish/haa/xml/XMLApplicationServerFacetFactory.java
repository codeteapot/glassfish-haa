package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;

import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerFacetBean;
import com.github.codeteapot.ironhoist.MachineFacetFactory;
import com.github.codeteapot.ironhoist.MachineFacetInstantiationException;
import com.github.codeteapot.ironhoist.machine.MachineContext;
import com.github.codeteapot.ironhoist.machine.MachineFacet;
import javax.xml.bind.annotation.XmlElement;

class XMLApplicationServerFacetFactory implements MachineFacetFactory {

  private static final String DEFAULT_USER = "glassfish";

  @XmlElement(
      name = "user",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String user;

  @XmlElement(
      name = "administration",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private XMLApplicationServerAdministrationFactory administrationFactory;

  @XmlElement(
      name = "execution",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private XMLApplicationServerExecutionFactory executionFactory;

  private XMLApplicationServerFacetFactory() {
    user = null;
    administrationFactory = null;
    executionFactory = null;
  }

  @Override
  public MachineFacet getFacet(MachineContext context)
      throws MachineFacetInstantiationException {
    return new ApplicationServerFacetBean(
        context.getRef(),
        ofNullable(administrationFactory)
            .map(this::withAdministration)
            .orElseGet(this::withoutAdministration)
            .apply(context),
        ofNullable(executionFactory)
            .map(this::withExecution)
            .orElseGet(this::withoutExecution)
            .apply(context));
  }

  private GetApplicationServerAdministrationClause withAdministration(
      XMLApplicationServerAdministrationFactory factory) {
    return context -> factory.getAdministration(context, ofNullable(user).orElse(DEFAULT_USER));
  }

  private GetApplicationServerAdministrationClause withoutAdministration() {
    return context -> null;
  }

  private GetApplicationServerExecutionClause withExecution(
      XMLApplicationServerExecutionFactory factory) {
    return context -> factory.getExecution(context, ofNullable(user).orElse(DEFAULT_USER));
  }

  private GetApplicationServerExecutionClause withoutExecution() {
    return context -> null;
  }
}

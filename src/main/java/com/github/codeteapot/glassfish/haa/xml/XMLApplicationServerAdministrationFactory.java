package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.Executors.newScheduledThreadPool;

import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerAdministrationBean;
import com.github.codeteapot.ironhoist.MachineFacetInstantiationException;
import com.github.codeteapot.ironhoist.machine.MachineContext;
import javax.xml.bind.annotation.XmlElement;

class XMLApplicationServerAdministrationFactory {

  private static final String DEFAULT_PUBLIC_KEY = "~/.ssh/id_rsa.pub";

  private static final int ADMINISTRATOR_EXECUTOR_CORE_POOL_SIZE = 20;

  @XmlElement(
      name = "name",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String name;
  
  @XmlElement(
      name = "asAdminPath",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String asAdminPath;

  @XmlElement(
      name = "publicKey",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String publicKey;

  @XmlElement(
      name = "user",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String user;

  @XmlElement(
      name = "passwords",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String passwords;

  private XMLApplicationServerAdministrationFactory() {
    name = null;
    publicKey = null;
    user = null;
    passwords = null;
  }

  public ApplicationServerAdministrationBean getAdministration(
      MachineContext context,
      String username) throws MachineFacetInstantiationException {
    return new ApplicationServerAdministrationBean(
        context,
        newScheduledThreadPool(ADMINISTRATOR_EXECUTOR_CORE_POOL_SIZE),
        ofNullable(asAdminPath)
            .orElseThrow(() -> new MachineFacetInstantiationException("Undefined asadmin path")),
        username,
        ofNullable(name)
            .orElseThrow(() -> new MachineFacetInstantiationException("Undefined name")),
        ofNullable(publicKey).orElse(DEFAULT_PUBLIC_KEY),
        ofNullable(user)
            .orElseThrow(() -> new MachineFacetInstantiationException("Undefined user")),
        ofNullable(passwords)
            .orElseThrow(() -> new MachineFacetInstantiationException("Undefined passwords")));
  }
}

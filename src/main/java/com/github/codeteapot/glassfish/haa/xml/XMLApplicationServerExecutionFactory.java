package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;

import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerExecutionBean;
import com.github.codeteapot.ironhoist.MachineFacetInstantiationException;
import com.github.codeteapot.ironhoist.machine.MachineContext;
import javax.xml.bind.annotation.XmlElement;

class XMLApplicationServerExecutionFactory {

  private static final String DEFAULT_AUTHORIZED_KEYS = "~/.ssh/authorized_keys";

  @XmlElement(
      name = "group",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String group;

  @XmlElement(
      name = "authorizedKeys",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String authorizedKeysPath;

  @XmlElement(
      name = "hostRetrieve",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String hostRetrievePath;

  @XmlElement(
      name = "installDir",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String installDir;

  @XmlElement(
      name = "nodeDir",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String nodeDir;

  private XMLApplicationServerExecutionFactory() {
    group = null;
    authorizedKeysPath = null;
    hostRetrievePath = null;
    installDir = null;
    nodeDir = null;
  }

  public ApplicationServerExecutionBean getExecution(
      MachineContext context,
      String username) throws MachineFacetInstantiationException {
    return new ApplicationServerExecutionBean(
        context,
        username,
        ofNullable(group)
            .orElseThrow(() -> new MachineFacetInstantiationException("Undefined group")),
        ofNullable(authorizedKeysPath).orElse(DEFAULT_AUTHORIZED_KEYS),
        ofNullable(hostRetrievePath)
            .orElseThrow(() -> new MachineFacetInstantiationException("Undefined host retrieve")),
        installDir,
        nodeDir);
  }
}

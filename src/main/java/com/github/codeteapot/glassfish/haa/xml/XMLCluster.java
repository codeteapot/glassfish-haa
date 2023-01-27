package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;

import com.github.codeteapot.glassfish.haa.Cluster;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationCluster;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationClusterName;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationDomain;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationDomainName;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerAdministration;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerExecution;
import java.util.Set;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

class XMLCluster implements Cluster {

  @XmlElement(
      name = "administrationName",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String administrationName;

  @XmlElementWrapper(
      name = "executionGroups",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  @XmlElement(
      name = "group",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private Set<String> executionGroups;

  @XmlElement(
      name = "domainName",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String domainName;

  @XmlElement(
      name = "clusterName",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String clusterName;

  private XMLCluster() {
    administrationName = null;
    executionGroups = null;
    domainName = null;
    clusterName = null;
  }

  @Override
  public boolean managedBy(ApplicationServerAdministration administration) {
    return ofNullable(administrationName)
        .map(administration.getName()::equals)
        .orElse(false);
  }

  @Override
  public boolean managedBy(ApplicationServerExecution execution) {
    return ofNullable(executionGroups)
        .map(groups -> groups.contains(execution.getGroup()))
        .orElse(false);
  }

  @Override
  public boolean managedBy(ApplicationDomain domain) {
    return ofNullable(domainName)
        .map(ApplicationDomainName::new)
        .map(domain.getName()::equals)
        .orElse(false);
  }

  @Override
  public boolean managedBy(ApplicationCluster cluster) {
    return ofNullable(clusterName)
        .map(ApplicationClusterName::new)
        .map(cluster.getName()::equals)
        .orElse(false);
  }
}

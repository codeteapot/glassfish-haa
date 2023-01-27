package com.github.codeteapot.glassfish.haa.xml;

import com.github.codeteapot.ironhoist.port.MachineProfileName;
import javax.xml.bind.annotation.XmlElement;

class XMLDockerRole {

  @XmlElement(
      name = "name",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String name;

  @XmlElement(
      name = "profile",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String profile;

  private XMLDockerRole() {
    name = null;
    profile = null;
  }

  boolean match(String name) {
    return name.equals(this.name);
  }

  MachineProfileName getProfileName() {
    return new MachineProfileName(profile);
  }
}

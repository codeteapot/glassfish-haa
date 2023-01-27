package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;

import com.github.codeteapot.ironhoist.MachineSessionPool;
import java.util.Optional;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.datatype.Duration;

class XMLMachineSessionPool implements MachineSessionPool {

  @XmlElement(
      name = "idleTimeout",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private Duration idleTimeout;

  private XMLMachineSessionPool() {
    idleTimeout = null;
  }

  @Override
  public Optional<java.time.Duration> getIdleTimeout() {
    return ofNullable(idleTimeout)
        .map(Duration::toString)
        .map(java.time.Duration::parse);
  }

  static XMLMachineSessionPool defaultInstance() {
    return new XMLMachineSessionPool();
  }
}

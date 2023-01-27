package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;

import com.github.codeteapot.ironhoist.MachineReadyCheck;
import com.github.codeteapot.ironhoist.session.MachineCommand;
import java.util.Optional;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.datatype.Duration;

class XMLMachineReadyCheck implements MachineReadyCheck {

  @XmlElement(
      name = "username",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String username;

  @XmlElement(
      name = "command",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private XMLMachineReadyCheckCommandFactory commandFactory;

  @XmlElement(
      name = "initialDelay",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private Duration initialDelay;
  
  @XmlElement(
      name = "retryDelay",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private Duration retryDelay;

  private XMLMachineReadyCheck() {
    username = null;
    commandFactory = null;
    initialDelay = null;
    retryDelay = null;
  }

  @Override
  public String getUsername() {
    return ofNullable(username)
        .orElseThrow(() -> new IllegalStateException("Undefined user name"));
  }

  @Override
  public Optional<MachineCommand<Boolean>> getCommand() {
    return ofNullable(commandFactory)
        .map(XMLMachineReadyCheckCommandFactory::getCommand);
  }

  @Override
  public Optional<java.time.Duration> getInitialDelay() {
    return ofNullable(initialDelay)
        .map(Duration::toString)
        .map(java.time.Duration::parse);
  }
  
  @Override
  public Optional<java.time.Duration> getRetryDelay() {
    return ofNullable(retryDelay)
        .map(Duration::toString)
        .map(java.time.Duration::parse);
  }
}

package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;

import com.github.codeteapot.ironhoist.port.MachineProfileName;
import com.github.codeteapot.ironhoist.port.PlatformPort;
import com.github.codeteapot.ironhoist.port.docker.DockerPlatformPort;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

class XMLDockerPlatformPortFactory implements PlatformPortFactory {

  @XmlElement(
      name = "group",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String group;

  @XmlElement(
      name = "host",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private URI host;

  @XmlElement(
      name = "eventsTimeout",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private Duration eventsTimeout;

  @XmlElementWrapper(
      name = "roles",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  @XmlElement(
      name = "role",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private List<XMLDockerRole> roles;

  private XMLDockerPlatformPortFactory() {
    group = null;
    roles = null;
  }

  @Override
  public PlatformPort getPort() {
    return new DockerPlatformPort(group, host, eventsTimeout, this::roleMapper);
  }

  private Optional<MachineProfileName> roleMapper(String roleName) {
    return ofNullable(roles)
        .map(List::stream)
        .orElseGet(Stream::empty)
        .filter(role -> role.match(roleName))
        .map(XMLDockerRole::getProfileName)
        .findAny();
  }
}

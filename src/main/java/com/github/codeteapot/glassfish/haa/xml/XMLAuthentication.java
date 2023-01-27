package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;

import com.github.codeteapot.ironhoist.session.MachineSessionClient;
import com.github.codeteapot.ironhoist.session.MachineSessionFactory;
import java.util.Set;
import java.util.stream.Stream;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

class XMLAuthentication {

  @XmlElementWrapper(
      name = "identities",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  @XmlElement(
      name = "identity",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private Set<XMLAuthenticationIdentity> identities;

  XMLAuthentication() {
    identities = null;
  }

  MachineSessionFactory getSessionFactory() {
    MachineSessionClient client = new MachineSessionClient();
    ofNullable(identities)
        .map(Set::stream)
        .orElseGet(Stream::empty)
        .forEach(identity -> identity.configure(client));
    return client;
  }
}

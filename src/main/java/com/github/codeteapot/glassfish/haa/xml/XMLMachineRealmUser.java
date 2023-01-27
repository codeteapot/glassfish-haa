package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;

import com.github.codeteapot.ironhoist.session.MachineSessionAuthentication;
import com.github.codeteapot.ironhoist.session.MachineSessionAuthenticationContext;
import com.github.codeteapot.ironhoist.session.MachineSessionIdentityName;
import com.github.codeteapot.ironhoist.session.MachineSessionPasswordName;
import java.util.List;
import java.util.stream.Stream;
import javax.xml.bind.annotation.XmlElement;

class XMLMachineRealmUser implements MachineSessionAuthentication {

  @XmlElement(
      name = "name",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String name;

  @XmlElement(
      name = "identityOnly",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String identityOnly;

  @XmlElement(
      name = "password",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private List<String> passwords;

  private XMLMachineRealmUser() {
    name = null;
    identityOnly = null;
    passwords = null;
  }

  boolean match(String username) {
    return ofNullable(name)
        .map(username::equals)
        .orElseThrow(() -> new IllegalArgumentException("Undefined user name"));
  }

  @Override
  public void authenticate(MachineSessionAuthenticationContext context) {
    ofNullable(identityOnly)
        .map(MachineSessionIdentityName::new)
        .ifPresent(context::setIdentityOnly);
    ofNullable(passwords)
        .map(List::stream)
        .orElseGet(Stream::empty)
        .map(MachineSessionPasswordName::new)
        .forEach(context::addPassword);
  }
}

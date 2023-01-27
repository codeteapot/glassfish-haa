package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;

import com.github.codeteapot.ironhoist.MachineRealm;
import com.github.codeteapot.ironhoist.session.MachineSessionAuthentication;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.xml.bind.annotation.XmlElement;

class XMLMachineRealm implements MachineRealm {

  @XmlElement(
      name = "user",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private List<XMLMachineRealmUser> users;

  private XMLMachineRealm() {
    users = null;
  }

  @Override
  public Optional<? extends MachineSessionAuthentication> authentication(String username) {
    return ofNullable(users)
        .map(List::stream)
        .orElseGet(Stream::empty)
        .filter(user -> user.match(username))
        .findAny();
  }
}

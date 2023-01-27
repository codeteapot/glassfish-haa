package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.github.codeteapot.ironhoist.session.MachineSessionClient;
import com.github.codeteapot.ironhoist.session.MachineSessionIdentityName;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlElement;

class XMLAuthenticationIdentity {

  private static final Logger logger = getLogger(XMLAuthenticationIdentity.class.getName());

  @XmlElement(
      name = "name",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String name;

  @XmlElement(
      name = "export",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private XMLAuthenticationIdentityExport export;

  void configure(MachineSessionClient client) {
    try (OutputStream publicKeyOutput = export.open()) {
      ofNullable(name)
          .map(MachineSessionIdentityName::new)
          .map(this::withName)
          .orElseGet(this::withoutName)
          .apply(client, publicKeyOutput);
    } catch (IOException e) {
      logger.log(SEVERE, "Could not configure authentication identity", e);
    }
  }

  private GenerateIdentityClause withName(MachineSessionIdentityName identityName) {
    return (client, publicKeyOutput) -> client.generateKeyPair(identityName, publicKeyOutput);
  }

  private GenerateIdentityClause withoutName() {
    return (client, publicKeyOutput) -> client.generateKeyPair(publicKeyOutput);
  }
}

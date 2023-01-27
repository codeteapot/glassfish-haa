package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import javax.xml.bind.annotation.XmlValue;

class XMLAuthenticationIdentityExport {

  @XmlValue
  private URI location;

  XMLAuthenticationIdentityExport() {
    location = null;
  }

  OutputStream open() throws IOException {
    return ofNullable(location)
        .orElseThrow(() -> new IllegalStateException("Undefined identity export location"))
        .toURL()
        .openConnection()
        .getOutputStream();
  }
}

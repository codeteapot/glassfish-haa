package com.github.codeteapot.glassfish.haa.xml;

import com.github.codeteapot.glassfish.haa.platform.FetchMachineBuilder;
import com.github.codeteapot.glassfish.haa.platform.MachineBuilderDeserializer;
import com.github.codeteapot.glassfish.haa.platform.MachineBuilderFactory;
import com.github.codeteapot.ironhoist.MachineBuilder;
import javax.xml.bind.annotation.XmlElement;

class XMLFetchMachineBuilderFactory implements MachineBuilderFactory {

  @XmlElement(
      name = "username",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String username;

  @XmlElement(
      name = "file",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String filePath;

  XMLFetchMachineBuilderFactory() {
    username = null;
    filePath = null;
  }

  @Override
  public MachineBuilder getBuilder(MachineBuilderDeserializer deserializer) {
    return new FetchMachineBuilder(deserializer, username, filePath);
  }
}

package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;

import com.github.codeteapot.glassfish.haa.platform.MachineBuilderDeserializer;
import com.github.codeteapot.glassfish.haa.platform.MachineBuilderFactory;
import com.github.codeteapot.glassfish.haa.platform.SimpleMachineProfile;
import com.github.codeteapot.ironhoist.port.MachineNetworkName;
import com.github.codeteapot.ironhoist.port.MachineProfileName;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

class XMLMachineProfileFactory {

  @XmlElement(
      name = "name",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String name;

  @XmlElement(
      name = "network",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private String network;

  @XmlElement(
      name = "port",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private Integer sessionPort;

  @XmlElement(
      name = "realm",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private XMLMachineRealm realm;

  @XmlElement(
      name = "ready",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private XMLMachineReadyCheck readyCheck;

  @XmlElement(
      name = "pool",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private XMLMachineSessionPool sessionPool;

  @XmlElements({
      @XmlElement(
          type = XMLDefineMachineBuilderFactory.class,
          name = "define",
          namespace = "http://codeteapot.github.io/glassfish-haa"),
      @XmlElement(
          type = XMLIncludeMachineBuilderFactory.class,
          name = "include",
          namespace = "http://codeteapot.github.io/glassfish-haa"),
      @XmlElement(
          type = XMLFetchMachineBuilderFactory.class,
          name = "fetch",
          namespace = "http://codeteapot.github.io/glassfish-haa")
  })
  private MachineBuilderFactory builderFactory;

  private XMLMachineProfileFactory() {
    name = null;
    network = null;
    sessionPort = null;
    realm = null;
    readyCheck = null;
    sessionPool = null;
    builderFactory = null;
  }

  Entry<MachineProfileName, SimpleMachineProfile> toEntry(MachineBuilderDeserializer deserializer) {
    return new SimpleEntry<>(new MachineProfileName(name), new SimpleMachineProfile(
        deserializer,
        new MachineNetworkName(network),
        sessionPort,
        realm,
        readyCheck,
        ofNullable(sessionPool).orElseGet(XMLMachineSessionPool::defaultInstance),
        builderFactory));
  }
}

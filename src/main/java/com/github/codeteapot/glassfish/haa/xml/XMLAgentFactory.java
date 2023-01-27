package com.github.codeteapot.glassfish.haa.xml;

import com.github.codeteapot.glassfish.haa.Agent;
import com.github.codeteapot.glassfish.haa.AgentFactory;
import javax.xml.bind.annotation.XmlElement;

public class XMLAgentFactory implements AgentFactory {

  @XmlElement(
      name = "availability",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private XMLClusterManagerFactory availability;

  @XmlElement(
      name = "platform",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private XMLPlatformContextFactory platform;

  @Override
  public Agent getAgent() {
    return new Agent(platform, availability);
  }
}

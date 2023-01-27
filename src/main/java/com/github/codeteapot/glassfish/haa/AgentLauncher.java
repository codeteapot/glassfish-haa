package com.github.codeteapot.glassfish.haa;

import static java.lang.String.format;
import static java.lang.System.getProperty;
import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.lang.management.ManagementFactory.getPlatformMBeanServer;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static javax.xml.bind.JAXBContext.newInstance;

import com.github.codeteapot.glassfish.haa.platform.MachineBuilderDeserializer;
import com.github.codeteapot.glassfish.haa.xml.XMLAgentFactory;
import com.github.codeteapot.glassfish.haa.xml.XMLMachineBuilder;
import java.io.File;
import java.io.IOException;
import javax.management.MBeanServer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

class AgentLauncher {

  private static final String CONFIGURATION_FILE_PROPERTY_NAME =
      "com.github.codeteapot.glassfish.haa.config.file";

  static AgentLauncherConstructor constructor = AgentLauncher::new;

  private final AgentFactory agentFactory;

  private AgentLauncher(AgentFactory agentFactory) {
    this.agentFactory = requireNonNull(agentFactory);
  }

  public static void main(String[] args) {
    try {
      JAXBContext jaxbContext = newInstance(
          XMLAgentFactory.class,
          XMLMachineBuilder.class);
      constructor.construct(jaxbContext.createUnmarshaller().unmarshal(
          ofNullable(getProperty(CONFIGURATION_FILE_PROPERTY_NAME))
              .map(File::new)
              .map(StreamSource::new)
              .orElseThrow(() -> new IllegalArgumentException(format(
                  "Missing configuration file property: %s",
                  CONFIGURATION_FILE_PROPERTY_NAME))),
          XMLAgentFactory.class).getValue()).launch(
              input -> {
                try {
                  return jaxbContext.createUnmarshaller()
                      .unmarshal(new StreamSource(input), XMLMachineBuilder.class)
                      .getValue();
                } catch (JAXBException e) {
                  throw new IOException(e);
                }
              },
              getPlatformMBeanServer());
    } catch (JAXBException e) {
      throw new IllegalArgumentException("Invalid configuration file", e);
    }
  }

  private void launch(
      MachineBuilderDeserializer machineBuilderDeserializer,
      MBeanServer beanServer) {
    try {
      agentFactory.getAgent().accept(machineBuilderDeserializer, beanServer);
    } catch (InterruptedException e) {
      out.println("Interrupted");
      currentThread().interrupt();
    }
  }
}

package com.github.codeteapot.glassfish.haa.xml;

import static java.lang.Thread.currentThread;
import static java.util.Optional.ofNullable;

import com.github.codeteapot.glassfish.haa.platform.MachineBuilderDeserializer;
import com.github.codeteapot.glassfish.haa.platform.PlatformContextFactory;
import com.github.codeteapot.ironhoist.PlatformAdapter;
import com.github.codeteapot.ironhoist.PlatformContext;
import com.github.codeteapot.ironhoist.PlatformEventSource;
import com.github.codeteapot.ironhoist.port.PlatformPort;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

class XMLPlatformContextFactory implements PlatformContextFactory {

  @XmlElementWrapper(
      name = "listen",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  @XmlElements({
      @XmlElement(
          type = XMLDockerPlatformPortFactory.class,
          name = "docker",
          namespace = "http://codeteapot.github.io/glassfish-haa")
  })
  private List<PlatformPortFactory> listen;

  @XmlElement(
      name = "catalog",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private XMLMachineCatalogFactory catalog;

  @XmlElement(
      name = "authentication",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private XMLAuthentication authentication;

  private XMLPlatformContextFactory() {
    listen = null;
    catalog = null;
    authentication = null;
  }

  @Override
  public PlatformContext getContext(
      MachineBuilderDeserializer machineBuilderDeserializer,
      PlatformEventSource eventSource,
      ExecutorService listenerExecutor) {
    PlatformAdapter adapter = new PlatformAdapter(
        eventSource,
        ofNullable(catalog)
            .map(factory -> factory.getCatalog(machineBuilderDeserializer))
            .orElseThrow(() -> new IllegalStateException("Undefined catalog")),
        ofNullable(authentication)
            .orElseGet(XMLAuthentication::new)
            .getSessionFactory());
    ofNullable(listen)
        .map(List::stream)
        .orElseGet(Stream::empty)
        .map(PlatformPortFactory::getPort)
        .map(port -> listenTask(adapter, port))
        .forEach(listenerExecutor::submit);
    return adapter.getContext();
  }

  private Runnable listenTask(PlatformAdapter adapter, PlatformPort port) {
    return () -> {
      try {
        adapter.listen(port);
      } catch (InterruptedException e) {
        currentThread().interrupt();
      }
    };
  }
}

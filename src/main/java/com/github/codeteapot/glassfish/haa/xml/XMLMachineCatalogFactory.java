package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

import com.github.codeteapot.glassfish.haa.platform.MachineBuilderDeserializer;
import com.github.codeteapot.glassfish.haa.platform.MapMachineCatalog;
import com.github.codeteapot.ironhoist.MachineCatalog;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;
import javax.xml.bind.annotation.XmlElement;

class XMLMachineCatalogFactory implements MachineCatalogFactory {

  @XmlElement(
      name = "profile",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private List<XMLMachineProfileFactory> profiles;

  private XMLMachineCatalogFactory() {
    profiles = null;
  }

  @Override
  public MachineCatalog getCatalog(MachineBuilderDeserializer builderDeserializer) {
    return ofNullable(profiles)
        .map(List::stream)
        .orElseGet(Stream::empty)
        .map(profile -> profile.toEntry(builderDeserializer))
        .collect(collectingAndThen(
            toMap(Entry::getKey, Entry::getValue), MapMachineCatalog::new));
  }
}

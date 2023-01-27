package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.github.codeteapot.ironhoist.MachineBuilder;
import com.github.codeteapot.ironhoist.MachineBuilderContext;
import com.github.codeteapot.ironhoist.MachineBuildingException;
import com.github.codeteapot.ironhoist.MachineFacetFactory;
import com.github.codeteapot.ironhoist.MachineFacetInstantiationException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(
    name = "machine",
    namespace = "http://codeteapot.github.io/glassfish-haa")
public class XMLMachineBuilder implements MachineBuilder {

  private static final Logger logger = getLogger(XMLMachineBuilder.class.getName());

  @XmlElementWrapper(
      name = "facets",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  @XmlElements({
      @XmlElement(
          type = XMLApplicationServerFacetFactory.class,
          name = "applicationServer",
          namespace = "http://codeteapot.github.io/glassfish-haa")
  })
  private Set<MachineFacetFactory> facets;

  protected XMLMachineBuilder() {
    facets = null;
  }

  @Override
  public void build(MachineBuilderContext context) throws MachineBuildingException {
    Iterator<MachineFacetFactory> it = ofNullable(facets)
        .map(Set::stream)
        .orElseGet(Stream::empty)
        .iterator();
    while (it.hasNext()) {
      try {
        context.register(it.next());
      } catch (MachineFacetInstantiationException e) {
        logger.log(SEVERE, "Could not register facet", e);
      }
    }
  }
}

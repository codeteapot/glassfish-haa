package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

import com.github.codeteapot.glassfish.haa.ClusterManager;
import com.github.codeteapot.glassfish.haa.ClusterManagerFactory;
import com.github.codeteapot.ironhoist.PlatformContext;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import javax.xml.bind.annotation.XmlElement;

class XMLClusterManagerFactory implements ClusterManagerFactory {

  @XmlElement(
      name = "cluster",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private List<XMLCluster> clusters;

  private XMLClusterManagerFactory() {
    clusters = null;
  }

  @Override
  public ClusterManager getClusterManager(PlatformContext context) {
    return new ClusterManager(context, ofNullable(clusters)
        .map(Collection::stream)
        .orElseGet(Stream::empty)
        .collect(toSet()));
  }
}

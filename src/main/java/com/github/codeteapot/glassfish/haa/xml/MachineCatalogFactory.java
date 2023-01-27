package com.github.codeteapot.glassfish.haa.xml;

import com.github.codeteapot.glassfish.haa.platform.MachineBuilderDeserializer;
import com.github.codeteapot.ironhoist.MachineCatalog;

interface MachineCatalogFactory {

  MachineCatalog getCatalog(MachineBuilderDeserializer builderDeserializer);
}

package com.github.codeteapot.glassfish.haa.xml;

import static java.util.Optional.ofNullable;

import com.github.codeteapot.glassfish.haa.platform.SimpleMachineReadyCheckCommand;
import com.github.codeteapot.ironhoist.session.MachineCommand;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

class XMLMachineReadyCheckCommandFactory {

  @XmlElement(
      name = "arg",
      namespace = "http://codeteapot.github.io/glassfish-haa")
  private List<String> arguments;

  private XMLMachineReadyCheckCommandFactory() {
    arguments = null;
  }

  MachineCommand<Boolean> getCommand() {
    return new SimpleMachineReadyCheckCommand(ofNullable(arguments)
        .orElseGet(Collections::emptyList));
  }
}

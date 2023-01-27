package com.github.codeteapot.glassfish.haa.platform;

import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

import com.github.codeteapot.ironhoist.session.MachineCommand;
import com.github.codeteapot.ironhoist.session.MachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;
import java.util.List;
import java.util.stream.Stream;

public class SimpleMachineReadyCheckCommand implements MachineCommand<Boolean> {

  private final List<String> arguments;

  public SimpleMachineReadyCheckCommand(List<String> arguments) {
    this.arguments = unmodifiableList(arguments);
  }

  @Override
  public String getSentence() {
    return ofNullable(arguments)
        .map(List::stream)
        .orElseGet(Stream::empty)
        .collect(joining(" "));
  }

  @Override
  public MachineCommandExecution<Boolean> getExecution(MachineCommandExecutionContext context) {
    return new SimpleMachineReadyCheckCommandExecution(context);
  }
}

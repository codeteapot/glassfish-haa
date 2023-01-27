package com.github.codeteapot.ironhoist.session;

public interface MachineCommand<R> {

  String getSentence();

  MachineCommandExecution<R> getExecution(MachineCommandExecutionContext context);
}

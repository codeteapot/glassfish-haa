package com.github.codeteapot.ironhoist.session;

import java.io.Closeable;

public interface MachineSession extends Closeable {

  <R> R execute(MachineCommand<R> command) throws MachineSessionException;

  MachineSessionFile file(String path) throws MachineSessionException;
}

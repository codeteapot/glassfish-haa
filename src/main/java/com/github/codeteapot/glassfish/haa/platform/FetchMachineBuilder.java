package com.github.codeteapot.glassfish.haa.platform;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.MachineBuilderContext;
import com.github.codeteapot.ironhoist.MachineBuildingException;
import com.github.codeteapot.ironhoist.machine.UnknownUserException;
import com.github.codeteapot.ironhoist.port.MachineSessionHostResolutionException;
import com.github.codeteapot.ironhoist.session.MachineSession;
import com.github.codeteapot.ironhoist.session.MachineSessionException;
import java.io.IOException;
import java.io.InputStream;

public class FetchMachineBuilder extends InputMachineBuilder {

  private final String username;
  private final String filePath;

  public FetchMachineBuilder(
      MachineBuilderDeserializer deserializer,
      String username,
      String filePath) {
    super(deserializer);
    this.username = requireNonNull(username);
    this.filePath = requireNonNull(filePath);
  }

  @Override
  public void build(MachineBuilderContext context) throws MachineBuildingException {
    try (
        MachineSession session = context.getSession(username);
        InputStream input = session.file(filePath).getInputStream()) {
      build(context, input);
    } catch (UnknownUserException
        | MachineSessionHostResolutionException
        | MachineSessionException
        | IOException e) {
      throw new MachineBuildingException(e);
    }
  }
}

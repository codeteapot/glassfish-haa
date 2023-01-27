package com.github.codeteapot.ironhoist.session;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class BufferedMachineCommandExecution<R>
    extends AbstractMachineCommandExecution<R> {

  private static final int REMAINING_BUFFER_SIZE = 1024;

  private final ByteArrayOutputStream remainingOutput;
  private final ByteArrayOutputStream remainingError;
  private final byte[] remainingBuffer;

  protected BufferedMachineCommandExecution(MachineCommandExecutionContext context) {
    super(context);
    remainingOutput = new ByteArrayOutputStream();
    remainingError = new ByteArrayOutputStream();
    remainingBuffer = new byte[REMAINING_BUFFER_SIZE];
  }

  @Override
  public final void acceptOutput(byte[] bytes, int len) throws IOException {
    accept(bytes, len, remainingOutput, this::acceptOutput);
  }

  @Override
  public final void acceptError(byte[] bytes, int len) throws IOException {
    accept(bytes, len, remainingError, this::acceptError);
  }

  @Override
  public final R mapResult(int exitCode) throws MachineSessionException {
    try {
      accept(remainingOutput, this::acceptOutput);
      accept(remainingError, this::acceptError);
      return mapCompleteResult(exitCode);
    } catch (IOException e) {
      throw new MachineSessionException(e);
    }
  }

  protected void acceptOutput(String line) throws IOException {}

  protected void acceptError(String line) throws IOException {}

  protected abstract R mapCompleteResult(int exitCode) throws MachineSessionException;

  private void accept(
      byte[] bytes,
      int len,
      ByteArrayOutputStream remaining,
      InputLineConsumer consumer) throws IOException {
    remaining.write(bytes, 0, len);
    try (
        ByteArrayInputStream input = new ByteArrayInputStream(remaining.toByteArray());
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            input,
            context.charset()))) {
      String line = reader.readLine();
      while (line != null) {
        consumer.accept(line);
        line = reader.readLine();
      }
      remaining.reset();
      int remainingLen = input.read(remainingBuffer, 0, REMAINING_BUFFER_SIZE);
      while (remainingLen > 0) {
        remaining.write(bytes, 0, remainingLen);
        remainingLen = input.read(remainingBuffer, 0, REMAINING_BUFFER_SIZE);
      }
    }
  }

  private void accept(ByteArrayOutputStream remaining, InputLineConsumer consumer)
      throws IOException {
    consumer.accept(new String(remaining.toByteArray(), context.charset()));
  }
}

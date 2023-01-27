package com.github.codeteapot.ironhoist.session;

import static java.lang.System.currentTimeMillis;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.logging.Logger.getLogger;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Optional;
import java.util.logging.Logger;

class SSHMachineSession implements MachineSession {

  private static final int CONNECTION_TIMEOUT = 2000;
  private static final int BUFFER_SIZE = 4 * 1024;

  private static final long EXECUTION_TIMEOUT_MILLIS = 8000L;

  private static final Logger logger = getLogger(SSHMachineSession.class.getName());

  private final JSch jsch;
  private final InetAddress host;
  private final Integer port;
  private final String username;
  private final MachineSessionAuthentication authentication;
  private Session jschSession;

  SSHMachineSession(
      JSch jsch,
      InetAddress host,
      Integer port,
      String username,
      MachineSessionAuthentication authentication) {
    this.jsch = requireNonNull(jsch);
    this.host = requireNonNull(host);
    this.port = port;
    this.username = requireNonNull(username);
    this.authentication = requireNonNull(authentication);
  }

  @Override
  public <R> R execute(MachineCommand<R> command) throws MachineSessionException {
    try {
      return execute((ChannelExec) getSession().openChannel("exec"), command);
    } catch (JSchException e) {
      throw new MachineSessionException(e);
    }
  }

  @Override
  public MachineSessionFile file(String path) throws MachineSessionException {
    try {
      return new SSHMachineSessionFile((ChannelSftp) getSession().openChannel("sftp"), path);
    } catch (JSchException e) {
      throw new MachineSessionException(e);
    }
  }

  public void close() throws IOException {
    jschSession.disconnect();
  }

  private Session getSession() throws MachineSessionException {
    try {
      if (jschSession == null || !jschSession.isConnected()) {
        jschSession = ofNullable(port)
            .map(this::withSpecifiedPort)
            .orElseGet(this::withDefaultPort)
            .perform(username, host);
        authentication.authenticate(new SSHMachineSessionAuthenticationContext(
            jschSession,
            this::passwordMapper));
        jschSession.connect(CONNECTION_TIMEOUT);
      }
      return jschSession;
    } catch (JSchException e) {
      throw new MachineSessionException(e);
    }
  }

  private GetSessionAction withSpecifiedPort(int port) {
    return (username, host) -> jsch.getSession(username, host.getHostAddress(), port);
  }

  private GetSessionAction withDefaultPort() {
    return (username, host) -> jsch.getSession(username, host.getHostAddress());
  }

  private Optional<byte[]> passwordMapper(MachineSessionPasswordName passwordName) {
    // TODO Add password mapping
    return Optional.empty();
  }

  private <R> R execute(ChannelExec jschChannel, MachineCommand<R> command)
      throws MachineSessionException {
    try {
      long initialTime = currentTimeMillis();
      String sentence = command.getSentence();
      logger.fine(new StringBuilder()
          .append("Executing SSH command: ").append(sentence)
          .toString());
      InputStream output = jschChannel.getInputStream();
      InputStream error = jschChannel.getErrStream();
      MachineCommandExecutionContext context = new SSHMachineCommandExecutionContext(
          jschChannel.getOutputStream());
      jschChannel.setCommand(sentence);
      jschChannel.connect(CONNECTION_TIMEOUT);
      MachineCommandExecution<R> execution = command.getExecution(context);
      byte[] buffer = new byte[BUFFER_SIZE];
      while (currentTimeMillis() - initialTime < EXECUTION_TIMEOUT_MILLIS) {
        if (output.available() > 0) {
          int len = output.read(buffer, 0, BUFFER_SIZE);
          if (len > 0) {
            execution.acceptOutput(buffer, len);
          }
        }
        if (error.available() > 0) {
          int len = error.read(buffer, 0, BUFFER_SIZE);
          if (len > 0) {
            execution.acceptError(buffer, len);
          }
        }
        if (jschChannel.isClosed() && output.available() == 0 && error.available() == 0) {
          int exitStatus = jschChannel.getExitStatus();
          if (exitStatus == -1) {
            throw new MachineSessionException("Exit status is not available");
          }
          logger.fine(new StringBuilder()
              .append("SSH command completed: ").append(sentence)
              .toString());
          return execution.mapResult(exitStatus);
        }
      }
      throw new MachineSessionException("Execution timeout");
    } catch (JSchException | IOException e) {
      throw new MachineSessionException(e);
    } finally {
      jschChannel.disconnect();
    }
  }
}

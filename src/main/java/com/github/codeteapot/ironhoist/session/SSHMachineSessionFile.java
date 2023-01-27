package com.github.codeteapot.ironhoist.session;

import static java.util.Objects.requireNonNull;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class SSHMachineSessionFile implements MachineSessionFile {

  private static final int CONNECTION_TIMEOUT = 1000;

  private final ChannelSftp jschChannel;
  private final String path;

  SSHMachineSessionFile(ChannelSftp jschChannel, String path) throws MachineSessionException {
    try {
      this.jschChannel = requireNonNull(jschChannel);
      this.path = requireNonNull(path);
      jschChannel.connect(CONNECTION_TIMEOUT);
    } catch (JSchException e) {
      throw new MachineSessionException(e);
    }
  }

  @Override
  public InputStream getInputStream() throws IOException {
    try {
      return jschChannel.get(path);
    } catch (SftpException e) {
      throw new IOException(e);
    }
  }

  @Override
  public OutputStream getOutputStream() throws IOException {
    try {
      return jschChannel.put(path);
    } catch (SftpException e) {
      throw new IOException(e);
    }
  }

  @Override
  public void close() throws IOException {
    jschChannel.disconnect();
  }
}

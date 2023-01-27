package com.github.codeteapot.ironhoist.session;

import static java.io.File.createTempFile;
import static java.lang.String.format;
import static java.lang.System.getProperty;
import static java.net.InetAddress.getLocalHost;
import static java.util.logging.Logger.getLogger;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

class SSHMachineSessionIdentity {

  private static final Logger logger = getLogger(SSHMachineSessionIdentity.class.getName());

  private final File privateKeyFile;

  SSHMachineSessionIdentity(KeyPair keyPair, OutputStream publicKeyOutput) throws IOException {
    keyPair.writePublicKey(
        publicKeyOutput,
        format("%s@%s", getProperty("user.name"), getLocalHost().getHostName()));
    privateKeyFile = createPrivateKeyFile(createTempFile("ssh-identity-pkey", ""), keyPair);
  }

  void addTo(JSch jsch) throws IOException {
    try {
      logger.fine("Identity added to SSH client");
      jsch.addIdentity(privateKeyFile.getAbsolutePath());
    } catch (JSchException e) {
      throw new IOException(e);
    }
  }

  private static File createPrivateKeyFile(File file, KeyPair keyPair) throws IOException {
    try (OutputStream output = new FileOutputStream(file)) {
      keyPair.writePrivateKey(output);
      return file;
    }
  }
}

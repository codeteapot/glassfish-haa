package com.github.codeteapot.ironhoist.session;

import static com.jcraft.jsch.KeyPair.genKeyPair;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MachineSessionClient implements MachineSessionFactory {

  static {
    // TODO Remove this when external host checker has been designed (default/programmatic flag?)
    JSch.setConfig("StrictHostKeyChecking", "no");
  }

  private final JSch jsch;
  private final Set<SSHMachineSessionIdentity> unnamedIdentities;
  private final Map<MachineSessionIdentityName, SSHMachineSessionIdentity> namedIdentityMap;

  public MachineSessionClient() {
    jsch = new JSch();
    unnamedIdentities = new HashSet<>();
    namedIdentityMap = new HashMap<>();
  }

  @Override
  public MachineSession getSession(
      InetAddress host,
      Integer port,
      String username,
      MachineSessionAuthentication authentication) {
    return new SSHMachineSession(jsch, host, port, username, authentication);
  }

  public void generateKeyPair(
      MachineSessionIdentityName identityName,
      OutputStream publicKeyOuput) throws IOException {
    namedIdentityMap.put(identityName, createIdentity(publicKeyOuput));
  }

  public void generateKeyPair(OutputStream publicKeyOutput) throws IOException {
    unnamedIdentities.add(createIdentity(publicKeyOutput));
  }

  private SSHMachineSessionIdentity createIdentity(OutputStream publicKeyOutput)
      throws IOException {
    try {
      SSHMachineSessionIdentity identity =
          new SSHMachineSessionIdentity(genKeyPair(jsch, KeyPair.RSA, 2048), publicKeyOutput);
      identity.addTo(jsch);
      return identity;
    } catch (JSchException e) {
      throw new IOException(e);
    }
  }
}

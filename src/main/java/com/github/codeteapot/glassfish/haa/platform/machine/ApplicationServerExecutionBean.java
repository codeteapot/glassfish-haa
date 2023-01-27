package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

import com.github.codeteapot.ironhoist.machine.MachineContext;
import com.github.codeteapot.ironhoist.machine.MachineRef;
import com.github.codeteapot.ironhoist.machine.UnknownUserException;
import com.github.codeteapot.ironhoist.port.MachineSessionHostResolutionException;
import com.github.codeteapot.ironhoist.session.MachineSession;
import com.github.codeteapot.ironhoist.session.MachineSessionException;
import com.github.codeteapot.ironhoist.session.MachineSessionFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Set;

public class ApplicationServerExecutionBean implements ApplicationServerExecution {

  private final HostRetrieveCommandFactory hostRetrieveCommandFactory;
  private final MachineContext context;
  private final String username;
  private final String group;
  private final String authorizedKeysPath;
  private final String installDir;
  private final String nodeDir;

  public ApplicationServerExecutionBean(
      MachineContext context,
      String username,
      String group,
      String authorizedKeysPath,
      String hostRetrievePath,
      String installDir,
      String nodeDir) {
    hostRetrieveCommandFactory = new HostRetrieveCommandFactory(hostRetrievePath);
    this.context = requireNonNull(context);
    this.username = requireNonNull(username);
    this.group = requireNonNull(group);
    this.authorizedKeysPath = requireNonNull(authorizedKeysPath);
    this.installDir = installDir;
    this.nodeDir = nodeDir;
  }

  @Override
  public MachineRef getMachineRef() {
    return context.getRef();
  }

  @Override
  public String getGroup() {
    return group;
  }

  @Override
  public ApplicationNodeRequirements getNodeRequirements() throws ApplicationServerException {
    try (MachineSession session = context.getSession(username)) {
      return new ApplicationNodeRequirements(
          session.execute(hostRetrieveCommandFactory.retrieveHost()),
          installDir,
          nodeDir);
    } catch (UnknownUserException
        | MachineSessionHostResolutionException
        | MachineSessionException
        | IOException e) {
      throw new ApplicationServerException(e);
    }
  }

  @Override
  public boolean addAuthorizedKey(SSHPublicKey publicKey) throws ApplicationServerException {
    try (MachineSession session = context.getSession(username)) {
      MachineSessionFile authorizedKeysFile = session.file(authorizedKeysPath);
      Set<SSHPublicKey> authorizedKeys = getAuthorizedKeys(authorizedKeysFile);
      if (authorizedKeys.add(publicKey)) {
        setAuthorizedKeys(authorizedKeysFile, authorizedKeys);
        return true;
      }
      return false;
    } catch (MachineSessionException
        | UnknownUserException
        | MachineSessionHostResolutionException
        | IOException e) {
      throw new ApplicationServerException(e);
    }
  }

  @Override
  public boolean removeAuthorizedKey(SSHPublicKey publicKey) throws ApplicationServerException {
    try (MachineSession session = context.getSession(username)) {
      MachineSessionFile authorizedKeysFile = session.file(authorizedKeysPath);
      Set<SSHPublicKey> authorizedKeys = getAuthorizedKeys(authorizedKeysFile);
      if (authorizedKeys.remove(publicKey)) {
        setAuthorizedKeys(authorizedKeysFile, authorizedKeys);
        return true;
      }
      return false;
    } catch (MachineSessionException
        | UnknownUserException
        | MachineSessionHostResolutionException
        | IOException e) {
      throw new ApplicationServerException(e);
    }
  }

  private Set<SSHPublicKey> getAuthorizedKeys(MachineSessionFile authorizedKeysFile)
      throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        authorizedKeysFile.getInputStream()))) {
      return reader.lines()
          .map(SSHPublicKey::new)
          .collect(toSet());
    }
  }

  private void setAuthorizedKeys(
      MachineSessionFile authorizedKeysFile,
      Set<SSHPublicKey> authorizedKeys) throws IOException {
    try (PrintWriter writer = new PrintWriter(authorizedKeysFile.getOutputStream())) {
      authorizedKeys.stream()
          .map(SSHPublicKey::getValue)
          .forEach(writer::println);
    }
  }
}

package com.github.codeteapot.ironhoist;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.machine.MachineContext;
import com.github.codeteapot.ironhoist.machine.MachineRef;
import com.github.codeteapot.ironhoist.machine.UnknownUserException;
import com.github.codeteapot.ironhoist.port.MachineLink;
import com.github.codeteapot.ironhoist.port.MachineNetworkName;
import com.github.codeteapot.ironhoist.port.MachineSessionHostResolutionException;
import com.github.codeteapot.ironhoist.session.MachineSession;
import com.github.codeteapot.ironhoist.session.MachineSessionFactory;

class MachineContextImpl implements MachineContext {

  private final MachineRef ref;
  private final MachineRealm realm;
  private final MachineLink link;
  private final MachineNetworkName networkName;
  private final Integer sessionPort;
  private final MachineSessionFactory sessionFactory;

  MachineContextImpl(
      MachineRef ref,
      MachineRealm realm,
      MachineLink link,
      MachineNetworkName networkName,
      Integer sessionPort,
      MachineSessionFactory sessionFactory) {
    this.ref = requireNonNull(ref);
    this.realm = requireNonNull(realm);
    this.link = requireNonNull(link);
    this.networkName = requireNonNull(networkName);
    this.sessionPort = sessionPort;
    this.sessionFactory = requireNonNull(sessionFactory);
  }

  @Override
  public MachineRef getRef() {
    return ref;
  }

  @Override
  public MachineSession getSession(String username)
      throws UnknownUserException, MachineSessionHostResolutionException {
    return sessionFactory.getSession(
        link.getSessionHost(networkName),
        sessionPort,
        username,
        realm.authentication(username)
            .orElseThrow(() -> new UnknownUserException(ref, username)));
  }
}

package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

class AsAdminTarget {

  private final String host;
  private final Integer port;
  private final String user;
  private final String passwordsPath;

  AsAdminTarget(String host, Integer port, String user, String passwordsPath) {
    this.host = host;
    this.port = port;
    this.user = requireNonNull(user);
    this.passwordsPath = requireNonNull(passwordsPath);
  }

  void sentenceAppend(StringBuilder sentence) {
    ofNullable(host).ifPresent(sentence.append(" --host=")::append);
    ofNullable(port).ifPresent(sentence.append(" --port=")::append);
    sentence.append(" --user=").append(user);
    sentence.append(" --passwordfile=").append(passwordsPath);
  }
}

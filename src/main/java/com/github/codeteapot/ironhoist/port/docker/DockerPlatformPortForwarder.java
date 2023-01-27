package com.github.codeteapot.ironhoist.port.docker;

import static java.lang.Character.digit;
import static java.lang.String.format;
import static java.util.Collections.singleton;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;

import com.github.codeteapot.ironhoist.port.MachineId;
import com.github.codeteapot.ironhoist.port.MachineLink;
import com.github.codeteapot.ironhoist.port.MachineManager;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import java.util.List;
import java.util.logging.Logger;

class DockerPlatformPortForwarder {

  private static final Logger logger = getLogger(DockerPlatformPortForwarder.class.getName());

  private final DockerClient client;
  private final MachineManager manager;
  private final DockerRoleMapper roleMapper;

  DockerPlatformPortForwarder(
      MachineManager manager,
      DockerRoleMapper profileNameMapper,
      DockerClient client) {
    this.manager = requireNonNull(manager);
    this.roleMapper = requireNonNull(profileNameMapper);
    this.client = requireNonNull(client);
  }

  void take(Container container) {
    try {
      manager.take(toMachineId(container.getId()), toMachineLink(container));
    } catch (IllegalArgumentException e) {
      logger.severe(e.getMessage());
    }
  }

  void take(String containerId) {
    List<Container> containers = client.listContainersCmd()
        .withIdFilter(singleton(containerId))
        .exec();
    if (containers.isEmpty()) {
      logger.warning(format("Unable to take container %s", containerId));
    } else {
      take(containers.get(0));
    }
  }

  void forget(String containerId) {
    manager.forget(toMachineId(containerId));
  }

  private MachineId toMachineId(String containerId) {
    return new MachineId(fromHex(containerId));
  }

  private MachineLink toMachineLink(Container container) {
    return new DockerMachineLink(container, roleMapper);
  }

  private static byte[] fromHex(String str) {
    int len = str.length();
    byte[] bytes = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      int i1 = digit(str.charAt(i), 16) << 4;
      int i2 = digit(str.charAt(i + 1), 16);
      bytes[i / 2] = (byte) (i1 + i2);
    }
    return bytes;
  }
}

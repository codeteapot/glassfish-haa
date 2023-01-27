package com.github.codeteapot.ironhoist.port.docker;

import static com.github.codeteapot.ironhoist.port.docker.DockerLabels.getRoleLabelValue;
import static java.lang.String.format;
import static java.net.InetAddress.getByName;
import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.port.MachineLink;
import com.github.codeteapot.ironhoist.port.MachineNetworkName;
import com.github.codeteapot.ironhoist.port.MachineProfileName;
import com.github.codeteapot.ironhoist.port.MachineSessionHostResolutionException;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.ContainerNetworkSettings;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

class DockerMachineLink implements MachineLink {

  private final Container container;
  private final DockerRoleMapper roleMapper;

  DockerMachineLink(Container container, DockerRoleMapper roleMapper) {
    this.container = requireNonNull(container);
    this.roleMapper = requireNonNull(roleMapper);
  }

  @Override
  public Optional<MachineProfileName> getProfileName() {
    return getRoleLabelValue(container)
        .flatMap(roleMapper::map);
  }

  @Override
  public InetAddress getSessionHost(MachineNetworkName networkName)
      throws MachineSessionHostResolutionException {
    try {
      return getByName(Optional.of(container)
          .map(Container::getNetworkSettings)
          .map(ContainerNetworkSettings::getNetworks)
          .map(networks -> networks.get(networkName.getValue()))
          .map(ContainerNetwork::getIpAddress)
          .orElseThrow(() -> new MachineSessionHostResolutionException(
              format("Could not determine IP of network %s", networkName))));
    } catch (UnknownHostException e) {
      throw new MachineSessionHostResolutionException(e);
    }
  }
}

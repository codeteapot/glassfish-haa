package com.github.codeteapot.ironhoist.port.docker;

import static com.github.codeteapot.ironhoist.port.docker.DockerLabels.groupFilter;
import static com.github.dockerjava.api.model.EventType.CONTAINER;
import static com.github.dockerjava.core.DefaultDockerClientConfig.createDefaultConfigBuilder;
import static com.github.dockerjava.core.DockerClientImpl.getInstance;
import static java.lang.String.valueOf;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.github.codeteapot.ironhoist.port.MachineManager;
import com.github.codeteapot.ironhoist.port.PlatformPort;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.logging.Logger;

public class DockerPlatformPort implements PlatformPort {

  private static final Duration DEFAULT_EVENTS_TIMEOUT = ofSeconds(20L);
  private static final Duration RESPONSE_TIMEOUT_PADDING = ofMillis(100L);

  private static final Logger logger = getLogger(DockerPlatformPort.class.getName());

  private final String group;
  private final URI host;
  private final Duration eventsTimeout;
  private final DockerRoleMapper roleMapper;

  public DockerPlatformPort(
      String group,
      URI host,
      Duration eventsTimeout,
      DockerRoleMapper roleMapper) {
    this.group = requireNonNull(group);
    this.host = host;
    this.eventsTimeout = ofNullable(eventsTimeout).orElse(DEFAULT_EVENTS_TIMEOUT);
    this.roleMapper = requireNonNull(roleMapper);
  }

  @Override
  public void listen(MachineManager manager) throws InterruptedException {
    try {
      logger.fine(new StringBuilder()
          .append("Listening Docker port; group: ").append(group)
          .toString());
      DefaultDockerClientConfig.Builder configBuilder = createDefaultConfigBuilder();
      ofNullable(host)
          .map(URI::toString)
          .ifPresent(configBuilder::withDockerHost);
      DefaultDockerClientConfig config = configBuilder.build();
      DockerClient client = getInstance(config, new ApacheDockerHttpClient.Builder()
          .dockerHost(config.getDockerHost())
          .sslConfig(config.getSSLConfig())
          .maxConnections(2) // SEVERE: Must be 2 at least (events + get container)
          .connectionTimeout(ofSeconds(20))
          .responseTimeout(eventsTimeout.plus(RESPONSE_TIMEOUT_PADDING))
          .build());
      DockerPlatformPortForwarder forwarder = new DockerPlatformPortForwarder(
          manager,
          roleMapper,
          client);
      Map<String, String> labels = groupFilter(group);
      client.listContainersCmd()
          .withLabelFilter(labels)
          .exec()
          .stream()
          .forEach(forwarder::take);
      Instant sinceTime = fromInfoTimestamp(client.infoCmd()
          .exec()
          .getSystemTime());
      while (true) {
        Instant untilTime = sinceTime.plus(eventsTimeout);
        client.eventsCmd()
            .withEventTypeFilter(CONTAINER)
            .withLabelFilter(labels)
            .withSince(toEventTimestamp(sinceTime))
            .withUntil(toEventTimestamp(untilTime))
            .exec(new DockerEventsResultCallback(forwarder))
            .awaitCompletion();
        sinceTime = untilTime;
      }
    } catch (RuntimeException e) {
      logger.log(SEVERE, "Docker port finished with error", e);
    }
  }

  private static Instant fromInfoTimestamp(String timestamp) {
    return ISO_DATE_TIME.parse(timestamp, Instant::from);
  }

  private static String toEventTimestamp(Instant instant) {
    return valueOf(instant.toEpochMilli() / 1000L);
  }
}

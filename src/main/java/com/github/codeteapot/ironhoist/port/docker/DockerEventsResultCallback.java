package com.github.codeteapot.ironhoist.port.docker;

import static java.util.Objects.requireNonNull;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Event;

class DockerEventsResultCallback extends ResultCallback.Adapter<Event> {

  private static final String ACTION_START = "start";
  private static final String ACTION_DIE = "die";

  private final DockerPlatformPortForwarder forwarder;

  DockerEventsResultCallback(DockerPlatformPortForwarder forwarder) {
    this.forwarder = requireNonNull(forwarder);
  }

  @Override
  public void onNext(Event event) {
    switch (event.getAction()) {
      case ACTION_START:
        forwarder.take(event.getId());
        break;
      case ACTION_DIE:
        forwarder.forget(event.getId());
        break;
      default:
        // Nothing to be done
    }
  }
}

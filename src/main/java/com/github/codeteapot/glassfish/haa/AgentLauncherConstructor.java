package com.github.codeteapot.glassfish.haa;

@FunctionalInterface
interface AgentLauncherConstructor {

  AgentLauncher construct(AgentFactory agentFactory);
}

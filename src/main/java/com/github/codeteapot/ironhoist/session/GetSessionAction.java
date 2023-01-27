package com.github.codeteapot.ironhoist.session;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.net.InetAddress;

@FunctionalInterface
interface GetSessionAction {

  Session perform(String usernane, InetAddress host) throws JSchException;
}

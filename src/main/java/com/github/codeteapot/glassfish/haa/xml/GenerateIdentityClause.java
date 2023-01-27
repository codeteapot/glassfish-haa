package com.github.codeteapot.glassfish.haa.xml;

import com.github.codeteapot.ironhoist.session.MachineSessionClient;
import java.io.IOException;
import java.io.OutputStream;

@FunctionalInterface
interface GenerateIdentityClause {

  void apply(MachineSessionClient client, OutputStream publicKeyOutput) throws IOException;
}

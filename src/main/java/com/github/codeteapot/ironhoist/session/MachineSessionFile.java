package com.github.codeteapot.ironhoist.session;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface MachineSessionFile extends Closeable {

  InputStream getInputStream() throws IOException;

  OutputStream getOutputStream() throws IOException;
}

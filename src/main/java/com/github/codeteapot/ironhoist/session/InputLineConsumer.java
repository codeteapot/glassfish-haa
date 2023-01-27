package com.github.codeteapot.ironhoist.session;

import java.io.IOException;

@FunctionalInterface
interface InputLineConsumer {
  
  void accept(String line) throws IOException;
}

package com.github.codeteapot.glassfish.haa.platform.machine;

import java.util.Random;

class PasswordsCodeGenerator {

  private final Random random;

  PasswordsCodeGenerator() {
    random = new Random();
  }

  String generate() {
    return random.ints('0', 'z' + 1)
        .filter(i -> (i <= '9' || i >= 'A') && (i <= 'Z' || i >= 'a'))
        .limit(20)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }
}

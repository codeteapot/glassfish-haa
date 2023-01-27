package com.github.codeteapot.glassfish.haa;

import com.github.codeteapot.ironhoist.PlatformContext;

interface Expectation {
  
  boolean satisfy(PlatformContext context);
}

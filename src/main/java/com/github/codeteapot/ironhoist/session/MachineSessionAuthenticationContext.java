package com.github.codeteapot.ironhoist.session;

public interface MachineSessionAuthenticationContext {
  
  void setIdentityOnly(MachineSessionIdentityName identityName);
  
  void addPassword(MachineSessionPasswordName passwordName);  
}

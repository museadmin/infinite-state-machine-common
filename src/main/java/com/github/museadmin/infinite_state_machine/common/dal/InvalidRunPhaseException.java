package com.github.museadmin.infinite_state_machine.common.dal;

public class InvalidRunPhaseException extends RuntimeException {
  public InvalidRunPhaseException(String message) {
    super(message);
  }
}

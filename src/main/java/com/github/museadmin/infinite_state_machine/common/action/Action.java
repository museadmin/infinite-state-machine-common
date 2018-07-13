package com.github.museadmin.infinite_state_machine.common.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parent for all actions.
 */
public class Action {

  private static final Logger LOGGER = LoggerFactory.getLogger(Action.class.getName());

  /**
   * Default constructor
   */
  public Action() {

  }

  /**
   * Activate an action.
   */
  private void activate() {

  }

  /**
   * Test if action is active.
   * @return True if action is active.
   */
  private Boolean active() {
    return true;
  }

  /**
   * Deactivate an action.
   */
  private void deactivate() {

  }

  /**
   * Return the payload for this action read from the database.
   */
  private void payload() {

  }


}

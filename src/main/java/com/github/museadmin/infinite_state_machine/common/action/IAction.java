package com.github.museadmin.infinite_state_machine.common.action;

import com.github.museadmin.infinite_state_machine.common.dal.DataAccessLayer;
import org.json.JSONObject;

import java.util.ArrayList;

public interface IAction {
  void execute();

  Boolean active();
  boolean active(String action);
  void activate(String actionName);
  Boolean afterActionsComplete();
  Boolean beforeActionsComplete();
  void clearPayload(String actionName);
  String createRunDirectory(String directory);
  void deactivate();
  void deactivate(String actionFlag);
  JSONObject getJsonObjectFromFile(String fileName);
  void insertProperty(String property, String value);
  void setDataAccessLayer(DataAccessLayer dataAccessLayer);
  void setRunRoot(String runRoot);
  String queryProperty(String property);
  String queryRunPhase();
  void setState(String stateName);
  void updatePayload(String actionName, String payload);
  void updateProperty(String property, String value);
  void updateRunPhase(String runPhase);
  void unsetState(String stateName);
}

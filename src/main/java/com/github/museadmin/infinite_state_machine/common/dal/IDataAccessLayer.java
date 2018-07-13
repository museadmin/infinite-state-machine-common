package com.github.museadmin.infinite_state_machine.common.dal;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IDataAccessLayer {
  void createDatabase(String database);
  void createTable(JSONObject table);
  void insertState(JSONArray state);
  void insertAction(JSONArray action);
  Boolean executeSqlStatement(String sql);
}

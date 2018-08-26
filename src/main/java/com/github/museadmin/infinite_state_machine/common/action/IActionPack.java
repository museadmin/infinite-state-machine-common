package com.github.museadmin.infinite_state_machine.common.action;

import com.github.museadmin.infinite_state_machine.common.dal.DataAccessLayer;
import com.github.museadmin.infinite_state_machine.common.lib.PropertyCache;
import org.json.JSONObject;

import java.util.ArrayList;

public interface IActionPack {
  boolean copyResourceFile(String resource, String destination);
  JSONObject getJsonObjectFromResourceFile(String fileName);
  ArrayList getActionsFromActionPack(
    DataAccessLayer dataAccessLayer,
    PropertyCache propertyCache
  );
}

package com.github.museadmin.infinite_state_machine.common.action;

import com.github.museadmin.infinite_state_machine.common.dal.DataAccessLayer;
import org.json.JSONObject;
import java.util.ArrayList;

public interface IActionPack {
  JSONObject getJsonObjectFromResourceFile(String fileName);
  ArrayList <IAction> getActionsFromActionPack(
    DataAccessLayer dataAccessLayer,
    String runRoot
  );
}

package com.github.museadmin.infinite_state_machine.common.action;

import org.json.JSONObject;
import java.util.ArrayList;

public interface IActionPack {
  JSONObject getJsonObjectFromResourceFile(String fileName);
  ArrayList getActionsFromActionPack();
}

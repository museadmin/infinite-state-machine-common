package com.github.museadmin.infinite_state_machine.common.action;

import org.json.JSONObject;

public interface IActionPack {
  JSONObject getJsonObjectFromResourceFile(String fileName);
}

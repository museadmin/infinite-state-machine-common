package com.github.museadmin.infinite_state_machine.common.action;

import com.github.museadmin.infinite_state_machine.core.InfiniteStateMachine;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ActionPack implements IActionPack {

  private static final Logger LOGGER = LoggerFactory.getLogger(InfiniteStateMachine.class.getName());

  /**
   * Read in a JSON file and return it as a JSONObject
   * @param fileName The unqualified file name for the resource
   * @return
   */
  public JSONObject getJsonObjectFromResourceFile(String fileName) {
    InputStream is = ClassLoader.getSystemResourceAsStream(fileName);
    InputStreamReader isr;
    BufferedReader br;
    StringBuilder sb = new StringBuilder();
    String content;
    try {
      isr = new InputStreamReader(is);
      br = new BufferedReader(isr);
      while ((content = br.readLine()) != null) {
        sb.append(content);
      }
      isr.close();
      br.close();
    } catch (IOException e) {
      LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
      System.exit(1);
    }
    return new JSONObject(sb.toString());
  }

}

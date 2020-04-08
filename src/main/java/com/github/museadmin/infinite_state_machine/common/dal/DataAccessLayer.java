package com.github.museadmin.infinite_state_machine.common.dal;

import com.github.museadmin.infinite_state_machine.common.lib.PropertyCache;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * The DAL mirrors the methods in each DAO and acts
 * as a pass through to the DAO in use
 */
public class DataAccessLayer implements IDataAccessObject {

  IDataAccessObject iDataAccessObject;

  /**
   * Create the control database for the state machine
   * @param propertyCache So we can get the connection details
   */
  public DataAccessLayer(PropertyCache propertyCache) {

    switch (propertyCache.getProperty("rdbms").toUpperCase()) {
      case "SQLITE3":
        String dbPath = propertyCache.getProperty("runRoot") +
            propertyCache.getProperty("dbPath");
        // Create the runtime directory for the db here as it isn't
        // needed for the other rdbms types.
        File dir = new File (dbPath);
        if (! dir.isDirectory()) {
          dir.mkdirs();
        }
        String dbFile = dbPath + File.separator + propertyCache.getProperty("dbName");
        iDataAccessObject = new Sqlite3(dbFile);
        break;
        // TODO reinstate when doing MySql layer
//      case "MYSQL":
//        iDataAccessObject = new Mysql(propertyCache);
//        break;
      default:
        throw new RuntimeException("Failed to identify RDBMS in use from property file");
    }
  }

  // ================= DB =================

  /**
   * Execute a SQL query and return the results in an array list
   * @param sql The query
   * @return ArrayList holds the records returned
   */
  public ArrayList<JSONObject> executeSqlQuery(String sql) {
    return iDataAccessObject.executeSqlQuery(sql);
  }

  /**
   * Executes a SQL statement
   * @param sql The statement to execute
   * @return True or False for success or failure
   */
  public Boolean executeSqlStatement(String sql) {
    return iDataAccessObject.executeSqlStatement(sql);
  }

  /**
   * Create a database table using a JSON definition
   * @param table JSONObject
   */
  public void createTable(String table) {
    iDataAccessObject.createTable(table);
  }

  // ================= Action =================

  /**
   * Test if this action is active
   * @return True or False for not active
   */
  public Boolean active(String actionName) {
    return iDataAccessObject.active(actionName);
  }

  /**
   * Activate an action.
   * @param actionName The name of the action to activate
   */
  public void activate(String actionName) {
    iDataAccessObject.activate(actionName);
  }

  /**
   * Deactivate an action.
   * @param actionName The name of the action to deactivate
   */
  public void deactivate(String actionName) {
    iDataAccessObject.deactivate(actionName);
  }

  /**
   * Clear the payload for an action prior to deactivation
   * @param actionName The name of the action
   */
  public void clearPayload(String actionName) {
    iDataAccessObject.clearPayload(actionName);
  }

  /**
   * Set the payload for an action
   * @param actionName The name of the action
   * @param payload The action's payload
   */
  public void updatePayload(String actionName, String payload) {
    iDataAccessObject.updatePayload(actionName, payload);
  }

  // ================= Hooks =================

  /**
   * Check if all "After" actions have completed so that we can
   * change state to STOPPED.
   * @return True if not all complete
   */
  public Boolean afterActionsComplete() {
    return iDataAccessObject.afterActionsComplete();
  }

  /**
   * Check if all "Before" actions have completed so that we can
   * change state to running.
   * @return True if all complete
   */
  public Boolean beforeActionsComplete() {
    return iDataAccessObject.beforeActionsComplete();
  }

  // ================= Property =================

  /**
   * Insert a new property into the properties table
   * @param property The name of the property
   * @param value The value of the property
   */
  public void insertProperty(String property, String value) {
    iDataAccessObject.insertProperty(property, value);
  }

  /**
   * Query a property in the properties table
   * @param property Name of the property
   * @return value of the property
   */
  public String queryProperty(String property) {
    return iDataAccessObject.queryProperty(property);
  }

  /**
   * Update an existing property in the properties table
   * @param property The name of the property
   * @param value The value of the property
   */
  public void updateProperty(String property, String value) {
    iDataAccessObject.updateProperty(property, value);
  }

  // ================= Run phase  =================

  /**
   * Return the active run phase
   * @return The name of the active run phase
   */
  public String queryRunPhase() {
    return iDataAccessObject.queryRunPhase();
  }

  /**
   * Set the run state. The run states are an option group
   * Hence the special method for setting these.
   * EMERGENCY_SHUTDOWN
   * NORMAL_SHUTDOWN
   * RUNNING
   * STARTING
   * STOPPED
   * @param runPhase Name of state to change to
   */
  public void updateRunPhase(String runPhase) {
    iDataAccessObject.updateRunPhase(runPhase);
  }

  // ================= State =================

  /**
   * Set a state in the state table
   * @param stateName The name of the state
   */
  public void setState(String stateName) {
    iDataAccessObject.setState(stateName);
  }

  /**
   * Unset a state in the state table
   * @param stateName The name of the state
   */
  public void unsetState(String stateName) {
    iDataAccessObject.unsetState(stateName);
  }

  // ================= Message  =================
  /**
   * Insert a message into the database. Assumes valid json object.
   * @param message JSONObject the message
   */
  public void insertMessage(JSONObject message) {
    iDataAccessObject.insertMessage(message);
  }

  /**
   * Retrieve an array of unprocessed messages form the database messages table
   * @return ArrayList of messages as JSONObjects
   */
  public ArrayList<JSONObject> getUnprocessedMessages() {
    return iDataAccessObject.getUnprocessedMessages();
  }

  /**
   * Set the processed field true of a message record
   * @param id The ID (PK) of the record
   */
  public void markMessageProcessed(Integer id) {
    iDataAccessObject.markMessageProcessed(id);
  }
}


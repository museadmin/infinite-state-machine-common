package com.github.museadmin.infinite_state_machine.common.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Extract Transform and Load SQL from a JSON file
 */
public class JsonToSqlEtl {

  // TODO This will need to have an interface and a different version for each RDBMS
  // or might be better to just move JsonToSqlEtl.parseSqlFromFile into each dao

  /**
   * Return an array list of SQL statements as parsed from an Action
   * pack's pack_data.json file that has been read into a JSONObject
   *
   * @param jsonObject The raw data  in json form
   * @return ArrayList of SQL statements
   */
  public static ArrayList <String> parseSqlFromJson(JSONObject jsonObject) {

    ArrayList <String> statements  = new ArrayList<>();
    JSONArray items = jsonObject.getJSONArray("items");

    for (int i = 0; i < items.length(); i++) {
      String crud = items.getJSONObject(i).getJSONObject("meta").get("crud").toString();
      if (crud.equals("insert")) {
        statements.addAll(parseInsertStatements(items.getJSONObject(i)));
      } else if (crud.equals("select")) {
        statements.add(parseSelectStatement(items.getJSONObject(i)));
      } else if (crud.equals("update")) {
        statements = null;
      } else if (crud.equals("delete")) {
        statements = null;
      }
    }

    return statements;
  }

  private static String parseSelectStatement(JSONObject jsonObject) {

    String tableName = jsonObject.getJSONObject("meta").get("table").toString();

    // Build the start of the select statement
    StringBuilder start = new StringBuilder();
    start.append("SELECT ");

    // Add the columns being selected
    JSONArray columns = jsonObject.getJSONArray("columns");
    for (int i = 0; i < columns.length(); i++) {
      start.append(columns.get(i) + ", ");
    }
    start = removeTrailingComma(start);

    // From
    start.append("FROM " + tableName + " ");
    String beginning = start.toString();

    // Do we have a where clause?
    StringBuilder whereClause = new StringBuilder();

    JSONObject where;
    if (jsonObject.has("where")) {
      whereClause.append("WHERE");
      where = jsonObject.getJSONObject("where");

      JSONArray predicates;
      if (where.has("predicates")) {
        predicates = where.getJSONArray("predicates");
        for (int i = 0; i < predicates.length(); i++) {
          JSONObject predicate = predicates.getJSONObject(i);
          String logicalOperator = (predicate.has("logical_operator")) ? predicate.getString("logical_operator") : "";
          String left = predicate.getString("left");
          String right = predicate.getString("right");
          String operator = predicate.getString("operator");
          whereClause.append(
            logicalOperator + " " +
            left + " " +
            operator + " " +
            "'" + right + "' ");
        }
      }
    } else {
      beginning = beginning.trim();
    }

    String end = ";";

    return beginning + whereClause.toString().trim() + end;
  }

  /**
   * Create SQL INSERT statements defined in an Action Pack's pack_data JSONObject
   * @param jsonObject One of the items from an Action Pack's pack_data file
   * @return ArrayList<String>The SQL INSERT statements</String>
   */
  private static ArrayList <String> parseInsertStatements(JSONObject jsonObject) {
    ArrayList <String> statements  = new ArrayList<>();
    String tableName = jsonObject.getJSONObject("meta").get("table").toString();

    // Build the front end of the insert statement
    StringBuilder start = new StringBuilder();
    start.append("INSERT INTO " + tableName + " (");
    JSONArray columns = jsonObject.getJSONArray("columns");
    for (int i = 0; i < columns.length(); i++) {
      start.append("'" + columns.get(i).toString() + "', ");
    }
    start = removeTrailingComma(start);
    start.append(") values (");
    String beginning = start.toString();

    // Create the end of the insert statement
    String end = ");";

    // Create the middle of each insert statement
    JSONArray valuesArray = jsonObject.getJSONArray("values");
    for (int i = 0; i < valuesArray.length(); i++) {
      StringBuilder middle = new StringBuilder();
      JSONArray values = valuesArray.getJSONArray(i);
      for (int j = 0; j < values.length(); j++) {
        middle.append("'" + values.get(j).toString() + "', ");
      }
      middle = removeTrailingComma(middle);

      // Push the statement into the statements list
      statements.add(beginning + middle.toString() + end);
    }

    return statements;
  }

  private static StringBuilder removeTrailingComma(StringBuilder target) {
    return target.deleteCharAt(target.toString().lastIndexOf(','));
  }
}

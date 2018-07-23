package com.github.museadmin.infinite_state_machine.common;

import com.github.museadmin.infinite_state_machine.common.utils.JsonToSqlEtl;
import junit.framework.Assert;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TestJsonToSqlEtl {

  protected JSONObject jsonObject;

  // Read resource file into JSONObject
  // Pass to ETL
  // Verify SQL select statements

  @Before
  public void setup() throws Exception {

    jsonObject = new JSONObject(
      IOUtils.toString(
        this.getClass().getResource("/test_data/sql_test_data.json"),
        "UTF-8"
      )
    );
  }

  @Test
  public void testEtlReturnsSqlSelectStatementWithoutWhereClause() {
    ArrayList<String> statements = JsonToSqlEtl.parseSqlFromJson(jsonObject);
    Assert.assertEquals(
      "SELECT status, state_flag, note FROM states;",
      statements.get(0));
  }

  @Test
  public void testEtlReturnsSqlSelectStatementWithWhereClause() {
    ArrayList<String> statements = JsonToSqlEtl.parseSqlFromJson(jsonObject);
    Assert.assertEquals(
      "SELECT status, state_flag, note FROM states " +
        "WHERE status = 'false';",
      statements.get(1));
  }

  @Test
  public void testEtlReturnsSqlSelectStatementWithWhereClauseAndAnotherclause() {
    ArrayList<String> statements = JsonToSqlEtl.parseSqlFromJson(jsonObject);
    Assert.assertEquals(
      "SELECT status, state_flag, note FROM states " +
        "WHERE status = 'false' AND state_flag = 'READY_TO_RUN';",
      statements.get(2));
  }

  @Test
  public void testEtlReturnsSqlSelectStatementWithWildcard() {
    ArrayList<String> statements = JsonToSqlEtl.parseSqlFromJson(jsonObject);
    Assert.assertEquals(
      "SELECT * FROM states;",
      statements.get(3));
  }

}

package com.egugue.licol.testutil

import com.egugue.licol.data.sqlite.briteDatabaseForTest
import com.squareup.sqlbrite.BriteDatabase
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.robolectric.RuntimeEnvironment

/**
 * A [TestRule] to execute regular process at tear down method when testing about SQLite.
 */
class SqliteTestingRule : TestRule{

  /**
   * a [BriteDatabase] for each test method
   */
  lateinit var briteDB: BriteDatabase

  override fun apply(base: Statement, description: Description): Statement {
    return object : Statement() {
      override fun evaluate() {
        try {
          briteDB = briteDatabaseForTest()
          base.evaluate()
        } finally {
          briteDB.close()
          RuntimeEnvironment.application.deleteDatabase("likedit")
        }
      }
    }
  }
}
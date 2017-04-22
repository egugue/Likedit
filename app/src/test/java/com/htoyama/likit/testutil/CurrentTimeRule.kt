package com.htoyama.likit.testutil

import com.htoyama.likit.common.CurrentTime
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.threeten.bp.*

/**
 * A test rule which we can override a value returned by [CurrentTime.get]
 *
 * @see Now
 */
class CurrentTimeRule : TestRule {

  private var locked: Boolean = false

  override fun apply(base: Statement, description: Description): Statement {
    return object : Statement() {
      override fun evaluate() {
        val now = description.getAnnotation(Now::class.java)
        if (now == null) {
          base.evaluate()
          return
        }

        try {
          lockCurrentTime(now)
          base.evaluate()
        } finally {
          unlockCurrentTime()
        }
      }
    }
  }

  private fun lockCurrentTime(a: Now) {
    if (locked) {
      throw IllegalMonitorStateException()
    }

    locked = true
    val zoneId = ZoneId.systemDefault()
    val instant = ZonedDateTime.of(a.year, a.month, a.dayOfMonth, a.hour, a.minute, a.second, a.nanoOfSecond, zoneId)
        .toInstant()
    CurrentTime.overrideTime(Clock.fixed(instant, zoneId))
  }

  private fun unlockCurrentTime() {
    if (!locked) {
      throw IllegalMonitorStateException()
    }

    CurrentTime.resetTime()
    locked = false
  }
}
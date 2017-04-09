package com.htoyama.likit.testutil

import com.htoyama.likit.common.CurrentTime
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.threeten.bp.LocalDateTime

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
          val p = createCurrentTimeProvider(now)
          lockCurrentTime(p)
          base.evaluate()
        } finally {
          unlockCurrentTime()
        }
      }
    }
  }

  fun createCurrentTimeProvider(a: Now): CurrentTime.Provider {
    return object : CurrentTime.Provider {
      override fun now(): LocalDateTime {
        return LocalDateTime.of(
            a.year, a.month, a.dayOfMonth, a.hour, a.minute, a.second, a.nanoOfSecond)
      }
    }
  }

  private fun lockCurrentTime(provider: CurrentTime.Provider) {
    if (locked) {
      throw IllegalMonitorStateException()
    }

    locked = true
    CurrentTime.overrideTime(provider)
  }

  private fun unlockCurrentTime() {
    if (!locked) {
      throw IllegalMonitorStateException()
    }

    CurrentTime.resetTime()
    locked = false
  }
}
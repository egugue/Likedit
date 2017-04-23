package com.htoyama.likit.testutil

import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * @see https://www.infoq.com/articles/Testing-RxJava
 */
class TestSchedulerRule : TestRule {

  private val scheduler = Schedulers.trampoline()

  override fun apply(base: Statement, description: Description?): Statement {
    return object : Statement() {
      override fun evaluate() {
        RxJavaPlugins.setNewThreadSchedulerHandler { scheduler }
        try {
          base.evaluate()
        } finally {
          RxJavaPlugins.reset()
        }
      }
    }
  }
}
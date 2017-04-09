package com.htoyama.likit.common

import android.support.annotation.VisibleForTesting
import org.threeten.bp.LocalDateTime

/**
 * A provider which provides the current [LocalDateTime]
 */
object CurrentTime {
  private val SYSTEM_PROVIDER: Provider = object : Provider {
    override fun now(): LocalDateTime = LocalDateTime.now()
  }

  private var provider: Provider = SYSTEM_PROVIDER

  /**
   * Return the current [LocalDateTime]
   */
  fun get(): LocalDateTime = provider.now()

  @VisibleForTesting @JvmStatic
  fun overrideTime(provider: Provider) {
    this.provider = provider
  }

  @VisibleForTesting @JvmStatic
  fun resetTime() {
    provider = SYSTEM_PROVIDER
  }

  @VisibleForTesting
  interface Provider {
    fun now(): LocalDateTime
  }
}
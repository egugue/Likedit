package com.egugue.licol.common

import android.support.annotation.VisibleForTesting
import org.threeten.bp.Clock
import org.threeten.bp.LocalDateTime

/**
 * A provider which provides the current [LocalDateTime]
 */
object CurrentTime {
  private val SYSTEM_CLOCK = Clock.systemDefaultZone()

  private var clock: Clock = SYSTEM_CLOCK

  /**
   * Return the current millisecond
   */
  fun get(): Long = clock.millis()

  @VisibleForTesting @JvmStatic
  internal fun overrideTime(clock: Clock) {
    this.clock = clock
  }

  @VisibleForTesting @JvmStatic
  internal fun resetTime() {
    clock = SYSTEM_CLOCK
  }
}
package com.egugue.licol

import android.os.StrictMode

import com.facebook.stetho.Stetho
import timber.log.Timber

/**
 * The application only using Debug mode.
 */
class DebugApp : App() {

  override fun onCreate() {
    super.onCreate()

    if (isNotUsingRobolectric()) {
      // if using Robolectric in local unit test, thrown IOException.
      // see https://github.com/facebook/stetho/issues/440
      Stetho.initializeWithDefaults(this)
    }

    applyStrictMode()
  }

  private fun plantTimberTree() {
    if (isNotUsingRobolectric()) {
      Timber.plant(Timber.DebugTree())
    } else {
      Timber.plant(TimberTree())
    }
  }

  private fun applyStrictMode() {
    StrictMode.setThreadPolicy(
        StrictMode.ThreadPolicy.Builder()
            .detectCustomSlowCalls()
            .detectDiskReads()
            .detectDiskWrites()
            .detectNetwork()
            .penaltyDialog()
            .penaltyLog()
            .penaltyFlashScreen()
            .build()
    )

    StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
        //.detectLeakedSqlLiteObjects()
        //.detectLeakedClosableObjects()
        .penaltyLog()
        //.penaltyDeath()
        .build())
  }

  private fun isNotUsingRobolectric(): Boolean {
    try {
      Class.forName("org.robolectric.RobolectricTestRunner")
      return false
    } catch (ignored: ClassNotFoundException) {
      return true
    }
  }
}

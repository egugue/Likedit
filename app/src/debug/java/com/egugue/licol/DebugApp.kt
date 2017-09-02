package com.egugue.licol

import android.os.StrictMode
import android.support.v4.content.ContextCompat
import com.facebook.stetho.Stetho
import jp.wasabeef.takt.Takt
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

      Takt.stock(this)
          .color(ContextCompat.getColor(this, android.R.color.black))
          .play()
    }

    plantTimberTree()
    applyStrictMode()
  }

  override fun onTerminate() {
    if (isNotUsingRobolectric()) {
      Takt.finish()
    }
    super.onTerminate()
  }

  private fun plantTimberTree() {
    if (isNotUsingRobolectric()) {
      Timber.plant(TimberTree())
    } else {
      Timber.plant(Timber.DebugTree())
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

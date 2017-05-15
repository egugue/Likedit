package com.egugue.licol

import android.util.Log
import com.google.firebase.crash.FirebaseCrash
import timber.log.Timber

class TimberTree : Timber.Tree() {

  override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
    if (priority == Log.VERBOSE || priority == Log.DEBUG) {
      return
    }

    FirebaseCrash.logcat(priority, tag, message)

    if (t != null) {
      if (priority == Log.ERROR || priority == Log.WARN) {
        FirebaseCrash.report(t)
      }
    }
  }
}

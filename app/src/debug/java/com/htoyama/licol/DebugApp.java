package com.htoyama.licol;

import android.os.StrictMode;

import com.facebook.stetho.Stetho;

/**
 * The application only using Debug mode.
 */
public class DebugApp extends App {

  @Override public void onCreate() {
    super.onCreate();

    if (isNotUsingRobolectric()) {
      // if using Robolectric in local unit test, thrown IOException.
      // see https://github.com/facebook/stetho/issues/440
      Stetho.initializeWithDefaults(this);
    }

    applyStrictMode();
  }

  private void applyStrictMode() {
    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectNetwork()
        .penaltyLog()
        .build());

    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectLeakedSqlLiteObjects()
        .detectLeakedClosableObjects()
        .detectActivityLeaks()
        .penaltyLog()
        .penaltyDeath()
        .build());
  }

  private boolean isNotUsingRobolectric() {
    try {
      Class.forName("org.robolectric.RobolectricTestRunner");
      return false;
    } catch (ClassNotFoundException ignored) {
      return true;
    }
  }
}

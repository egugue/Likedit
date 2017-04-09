package com.htoyama.likit.data.prefs

import com.htoyama.likit.data.common.pref.PrefModule
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.threeten.bp.LocalDateTime

@RunWith(RobolectricTestRunner::class)
class AppSettingTest {
  lateinit var setting: AppSetting

  @Before fun setUp() {
    val module = PrefModule()
    val prefs = module.pref(RuntimeEnvironment.application)
    setting = AppSetting(prefs, module.rxPrefs(prefs))
  }

  @Test fun lastSyncedDate() {
    val lastSynced = LocalDateTime.of(2017, 2, 28, 2, 30)

    setting.setLastSyncedDate(lastSynced)

    setting.getLastSyncedDate()
        .test()
        .assertValue(lastSynced)
  }

  @Test fun lastSyncedDate_whenSetMethodNeverInvoked() {
    setting.getLastSyncedDate()
        .test()
        .assertEmpty()
  }
}
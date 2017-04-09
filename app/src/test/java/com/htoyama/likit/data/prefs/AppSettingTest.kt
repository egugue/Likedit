package com.htoyama.likit.data.prefs

import com.htoyama.likit.data.common.pref.PrefModule
import com.htoyama.likit.testutil.CurrentTimeRule
import com.htoyama.likit.testutil.Now
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.threeten.bp.LocalDateTime

@RunWith(RobolectricTestRunner::class)
class AppSettingTest {
  @Rule @JvmField val rule: CurrentTimeRule = CurrentTimeRule()

  lateinit var setting: AppSetting

  @Before fun setUp() {
    val module = PrefModule()
    val prefs = module.pref(RuntimeEnvironment.application)
    setting = AppSetting(prefs, module.rxPrefs(prefs))
  }

  @Now(2017, 2, 28, 2, 30)
  @Test fun lastSyncedDate() {
    setting.setLastSyncedDateAsNow()

    setting.getLastSyncedDate()
        .test()
        .assertValue(LocalDateTime.of(2017, 2, 28, 2, 30))
  }

  @Test fun lastSyncedDate_whenSetMethodNeverInvoked() {
    setting.getLastSyncedDate()
        .test()
        .assertEmpty()
  }
}
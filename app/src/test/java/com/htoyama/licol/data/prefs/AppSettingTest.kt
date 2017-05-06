package com.htoyama.licol.data.prefs

import com.htoyama.licol.testutil.CurrentTimeRule
import com.htoyama.licol.testutil.Now
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
  @Test fun lastSyncedDate_shouldSaveCurrentTime() {
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

  @Test fun lastSyncedDate_shouldReceiveSpecifiedNumber() {
    val test = setting.getLastSyncedDate().test()

    setting.setLastSyncedDateAsNow()
    Thread.sleep(10) // seems like sleep is needed here to test emitted count correctly.
    setting.setLastSyncedDateAsNow()
    Thread.sleep(10)
    setting.setLastSyncedDateAsNow()

    test.assertValueCount(3)
  }
}
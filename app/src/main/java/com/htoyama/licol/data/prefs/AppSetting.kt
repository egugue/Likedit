package com.htoyama.licol.data.prefs

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.htoyama.licol.common.AllOpen
import com.htoyama.licol.common.CurrentTime
import io.reactivex.Observable
import org.threeten.bp.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A setting related to the whole application.
 */
@Singleton @AllOpen
class AppSetting @Inject constructor(
    private val prefs: SharedPreferences,
    private val rxPrefs: RxSharedPreferences
) {

  fun setLastSyncedDateAsNow() {
    val currentDateLong = CurrentTime.get()

    prefs.edit()
        .putLong("lastSyncedDate", currentDateLong)
        .apply()
  }

  fun getLastSyncedDate(): Observable<LocalDateTime> {
    return rxPrefs.getLong("lastSyncedDate", -1L)
        .asObservable()
        .filter { it != -1L }
        .map { LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault()) }
  }
}
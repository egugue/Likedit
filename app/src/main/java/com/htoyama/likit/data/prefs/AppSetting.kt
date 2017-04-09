package com.htoyama.likit.data.prefs

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import org.threeten.bp.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A setting related to the whole application.
 */
@Singleton
class AppSetting @Inject constructor(
    private val prefs: SharedPreferences,
    private val rxPrefs: RxSharedPreferences
) {

  fun setLastSyncedDate(date: LocalDateTime) {
    val dateLong = ZonedDateTime.of(date, ZoneId.systemDefault())
        .toInstant().toEpochMilli()

    prefs.edit()
        .putLong("lastSyncedDate", dateLong)
        .apply()
  }

  fun getLastSyncedDate(): Observable<LocalDateTime> {
    return rxPrefs.getLong("lastSyncedDate", -1L)
        .asObservable()
        .filter { it != -1L }
        .map { LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault()) }
  }
}
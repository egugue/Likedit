package com.htoyama.likit.data.sqlite

import com.htoyama.likit.data.sqlite.lib.SqliteOpenHelper
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import org.robolectric.RuntimeEnvironment
import rx.schedulers.Schedulers

fun briteDatabaseForTest(): BriteDatabase =
  SqlBrite.Builder().build().wrapDatabaseHelper(
          SqliteOpenHelper(RuntimeEnvironment.application),
          Schedulers.immediate())

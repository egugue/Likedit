package com.egugue.licol.data.sqlite

import com.egugue.licol.BuildConfig
import com.egugue.licol.data.sqlite.lib.SqliteOpenHelper
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import dagger.Module
import dagger.Provides
import rx.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Singleton

@Module class SqliteModule {

  @Provides @Singleton fun sqlBrite(): SqlBrite {
    return if (BuildConfig.DEBUG) {
      SqlBrite.Builder()
          //.logger { /* do nothing */ }
          .logger { Timber.i(it) }
          .build()
    } else {
      return SqlBrite.Builder()
          .logger { /* do nothing */ }
          .build()
    }
  }

  @Provides @Singleton fun database(helper: SqliteOpenHelper, sqlBrite: SqlBrite): BriteDatabase {
    val db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.immediate())
    if (BuildConfig.DEBUG) {
      db.setLoggingEnabled(true)
    }
    return db
  }
}
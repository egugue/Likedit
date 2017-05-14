package com.htoyama.licol.data.sqlite

import android.util.Log
import com.htoyama.licol.BuildConfig
import com.htoyama.licol.data.sqlite.lib.SqliteOpenHelper
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import dagger.Module
import dagger.Provides
import rx.schedulers.Schedulers
import javax.inject.Singleton

@Module class SqliteModule {

  @Provides @Singleton fun sqlBrite(): SqlBrite {
    // TODO: Use Timber instead of built-in Log
    return if (BuildConfig.DEBUG) {
      SqlBrite.Builder()
          .logger { Log.d("--", it) }
          .build()
    } else {
      SqlBrite.Builder().build()
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
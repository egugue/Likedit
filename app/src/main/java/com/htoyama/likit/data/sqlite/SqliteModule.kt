package com.htoyama.likit.data.sqlite

import dagger.Module

@Module class SqliteModule {

  /*
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
    val db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io())
    if (BuildConfig.DEBUG) {
      db.setLoggingEnabled(true)
    }
    return db
  }
  */
}
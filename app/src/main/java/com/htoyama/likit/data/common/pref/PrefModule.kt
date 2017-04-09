package com.htoyama.likit.data.common.pref

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module class PrefModule {

  @Provides @Singleton fun pref(app: Application): SharedPreferences =
      app.getSharedPreferences("likit", Context.MODE_PRIVATE)

  @Provides @Singleton fun rxPrefs(pref: SharedPreferences) =
      RxSharedPreferences.create(pref)

}


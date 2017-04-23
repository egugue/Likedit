package com.htoyama.likit;

import android.app.Application
import com.htoyama.likit.data.DataModule
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton

@Module(includes = arrayOf(DataModule::class))
class AppModule(val app: Application) {

  @Singleton @Provides fun app(): Application = app

}
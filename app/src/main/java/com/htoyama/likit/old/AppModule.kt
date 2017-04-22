package com.htoyama.likit.old;

import android.app.Application
import com.htoyama.likit.data.DataModule
import dagger.Module;
import dagger.Provides;

@Module(includes = arrayOf(DataModule::class))
class AppModule(val app: Application) {

  /*
  @Provides fun app(): Application = app
  */

}
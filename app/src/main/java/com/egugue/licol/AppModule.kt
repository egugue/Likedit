package com.egugue.licol;

import android.app.Application
import com.egugue.licol.data.DataModule
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton

@Module(includes = arrayOf(DataModule::class))
class AppModule(val app: Application) {

  @Singleton @Provides fun app(): Application = app

  @Singleton @Provides fun firebaseAnalytics() = FirebaseAnalytics.getInstance(app)
}
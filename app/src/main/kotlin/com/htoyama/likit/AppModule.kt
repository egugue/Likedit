package com.htoyama.likit;

import android.app.Application
import com.htoyama.likit.data.DataModule
import com.htoyama.likit.data.pref.AccessTokenPrefsDao
import dagger.Module;
import dagger.Provides;
import twitter4j.Twitter
import twitter4j.TwitterFactory
import javax.inject.Singleton

@Module(includes = arrayOf(DataModule::class))
class AppModule(val app: Application) {

  @Provides fun app(): Application = app

  @Provides @Singleton fun twitter(dao: AccessTokenPrefsDao): Twitter =
      TwitterFactory.getSingleton().apply {
        setOAuthConsumer(
            app.getString(R.string.twitter_secret_key),
            app.getString(R.string.twitter_secret_token))

        val accessToken = dao.get()
        if (accessToken != null) {
          oAuthAccessToken = accessToken
        }
      }

}
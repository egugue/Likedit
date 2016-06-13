package com.htoyama.likit;

import android.app.Application
import dagger.Module;
import dagger.Provides;
import twitter4j.Twitter
import twitter4j.TwitterFactory
import javax.inject.Singleton

@Module
class AppModule(val app: Application) {
  @Provides fun app(): Application = app

  @Singleton @Provides fun twitter(): Twitter {
    return TwitterFactory.getSingleton().apply {
      setOAuthConsumer(
          app.getString(R.string.twitter_secret_key),
          app.getString(R.string.twitter_secret_token))
    }
  }
}
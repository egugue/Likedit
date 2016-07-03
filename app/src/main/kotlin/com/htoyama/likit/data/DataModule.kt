package com.htoyama.likit.data

import com.htoyama.likit.data.common.net.FavoriteService
import com.htoyama.likit.data.common.net.NetModule
import com.htoyama.likit.data.common.pref.PrefModule
import com.htoyama.likit.data.tweet.TweetRepositoryImpl
import com.htoyama.likit.domain.tweet.TweetFactory
import com.htoyama.likit.domain.tweet.TweetRepository
import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(
    PrefModule::class,
    NetModule::class
))
class DataModule {

  @Provides fun tweetRepository(favoriteService: FavoriteService, factory: TweetFactory)
      : TweetRepository = TweetRepositoryImpl(favoriteService, factory)

}

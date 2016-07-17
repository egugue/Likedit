package com.htoyama.likit.data

import com.htoyama.likit.data.common.net.NetModule
import com.htoyama.likit.data.common.pref.PrefModule
import com.htoyama.likit.data.likedtweet.LikedRealmGateway
import com.htoyama.likit.data.likedtweet.LikedTweetRepositoryImpl
import com.htoyama.likit.data.likedtweet.RealmTweetFactory
import com.htoyama.likit.data.tag.TagRealmDao
import com.htoyama.likit.data.tag.TagRepositoryImpl
import com.htoyama.likit.data.likedtweet.tweet.LikedTweetDao
import com.htoyama.likit.domain.likedtweet.LikedTweetRepository
import com.htoyama.likit.domain.tag.TagRepository
import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(
    PrefModule::class,
    NetModule::class
))
class DataModule {

  @Provides fun tagRepository(tagRealmDao: TagRealmDao)
      : TagRepository = TagRepositoryImpl(tagRealmDao)

  @Provides fun likedTweetRepository(likedTweetDao: LikedTweetDao,
      likedRealmGateway: LikedRealmGateway, realmTweetFactory: RealmTweetFactory)
      : LikedTweetRepository
    = LikedTweetRepositoryImpl(likedTweetDao, likedRealmGateway, realmTweetFactory)

}

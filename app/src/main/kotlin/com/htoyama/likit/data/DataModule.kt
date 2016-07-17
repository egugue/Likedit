package com.htoyama.likit.data

import com.htoyama.likit.data.common.net.NetModule
import com.htoyama.likit.data.common.pref.PrefModule
import com.htoyama.likit.data.liked.LikedRealmGateway
import com.htoyama.likit.data.liked.LikedRepositoryImpl
import com.htoyama.likit.data.liked.LikedFactory
import com.htoyama.likit.data.tag.TagRealmDao
import com.htoyama.likit.data.tag.TagRepositoryImpl
import com.htoyama.likit.data.liked.tweet.LikedTweetDao
import com.htoyama.likit.domain.liked.LikedRepository
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
                                     likedRealmGateway: LikedRealmGateway, likedFactory: LikedFactory)
      : LikedRepository
    = LikedRepositoryImpl(likedTweetDao, likedRealmGateway, likedFactory)

}

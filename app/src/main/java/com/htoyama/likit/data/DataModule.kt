package com.htoyama.likit.data

import com.htoyama.likit.data.common.net.NetModule
import com.htoyama.likit.data.common.pref.PrefModule
import com.htoyama.likit.data.liked.LikedRealmGateway
import com.htoyama.likit.data.liked.LikedRepositoryImpl
import com.htoyama.likit.data.tag.TagRealmDao
import com.htoyama.likit.data.tag.TagRepositoryImpl
import com.htoyama.likit.data.liked.tweet.LikedTweetDao
import com.htoyama.likit.data.sqlite.SqliteModule
import com.htoyama.likit.data.user.UserRealmDao
import com.htoyama.likit.data.user.UserRepositoryImpl
import com.htoyama.likit.domain.liked.LikedRepository
import com.htoyama.likit.domain.tag.TagRepository
import com.htoyama.likit.domain.user.UserRepository
import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(
    PrefModule::class,
    NetModule::class,
    SqliteModule::class
))
class DataModule {

  @Provides fun tagRepository(tagRealmDao: TagRealmDao)
      : TagRepository = TagRepositoryImpl(tagRealmDao)

  @Provides fun likedTweetRepository(likedTweetDao: LikedTweetDao,
                                     likedRealmGateway: LikedRealmGateway)
      : LikedRepository
    = LikedRepositoryImpl(likedTweetDao, likedRealmGateway)

  @Provides fun userRepository(userRealmDao: UserRealmDao)
      : UserRepository = UserRepositoryImpl(userRealmDao)
}

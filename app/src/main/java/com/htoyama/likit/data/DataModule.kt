package com.htoyama.likit.data

import com.htoyama.likit.data.common.net.NetModule
import com.htoyama.likit.data.common.pref.PrefModule
import com.htoyama.likit.data.sqlite.SqliteModule
import com.htoyama.likit.domain.likedtweet.LikedTweetRepository
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

  @Provides fun tagRepository(): TagRepository = TagRepositoryImpl()

  @Provides fun likedTweetRepository(): LikedTweetRepository = LikedTweetRepositoryImpl()

  @Provides fun userRepository(): UserRepository = UserRepositoryImpl()
}

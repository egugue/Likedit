package com.htoyama.likit.data

import com.htoyama.likit.data.net.NetModule
import com.htoyama.likit.data.prefs.PrefModule
import com.htoyama.likit.data.sqlite.SqliteModule
import com.htoyama.likit.data.sqlite.likedtweet.LikedTweetSqliteDao
import com.htoyama.likit.data.sqlite.tag.TagSqliteDao
import com.htoyama.likit.data.sqlite.user.UserSqliteDao
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

  @Provides fun likedTweetRepository(dao: LikedTweetSqliteDao): LikedTweetRepository = LikedTweetRepositoryImpl(dao)
  @Provides fun tagRepository(dao: TagSqliteDao): TagRepository = TagRepositoryImpl(dao)
  @Provides fun userRepository(dao: UserSqliteDao): UserRepository = UserRepositoryImpl(dao)
}

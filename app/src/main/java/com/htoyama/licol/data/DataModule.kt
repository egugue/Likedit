package com.htoyama.licol.data

import com.htoyama.licol.data.net.NetModule
import com.htoyama.licol.data.prefs.PrefModule
import com.htoyama.licol.data.sqlite.SqliteModule
import com.htoyama.licol.data.sqlite.likedtweet.LikedTweetSqliteDao
import com.htoyama.licol.data.sqlite.tag.TagSqliteDao
import com.htoyama.licol.data.sqlite.user.UserSqliteDao
import com.htoyama.licol.domain.likedtweet.LikedTweetRepository
import com.htoyama.licol.domain.tag.TagRepository
import com.htoyama.licol.domain.user.UserRepository
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

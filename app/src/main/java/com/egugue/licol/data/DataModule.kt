package com.egugue.licol.data

import com.egugue.licol.data.net.NetModule
import com.egugue.licol.data.prefs.PrefModule
import com.egugue.licol.data.sqlite.SqliteModule
import com.egugue.licol.data.sqlite.likedtweet.LikedTweetSqliteDao
import com.egugue.licol.data.sqlite.tag.TagSqliteDao
import com.egugue.licol.data.sqlite.user.UserSqliteDao
import com.egugue.licol.domain.likedtweet.LikedTweetRepository
import com.egugue.licol.domain.tag.TagRepository
import com.egugue.licol.domain.user.UserRepository
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

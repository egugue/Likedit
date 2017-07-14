package com.egugue.licol

import com.egugue.licol.common.analytics.Analytics
import com.egugue.licol.data.net.FavoriteService
import com.egugue.licol.data.prefs.AppSetting
import com.egugue.licol.data.sqlite.likedtweet.LikedTweetTableGateway
import com.egugue.licol.data.sqlite.user.UserTableGateway
import com.egugue.licol.domain.likedtweet.LikedTweetRepository
import com.egugue.licol.domain.tag.TagRepository
import com.egugue.licol.domain.user.UserRepository
import com.egugue.licol.ui.auth.AuthActivity
import com.squareup.sqlbrite2.BriteDatabase
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface  AppComponent {
  fun inject(activity: AuthActivity)
  fun inject(activity: MainActivity)

  fun analytics(): Analytics
  fun tweetTableGateway(): LikedTweetTableGateway
  fun userTableGateway(): UserTableGateway
  fun briteDatabase(): BriteDatabase
  fun appSetting(): AppSetting
  fun favoriteService(): FavoriteService
  fun likedRepository(): LikedTweetRepository
  fun tagRepository(): TagRepository
  fun userRepository(): UserRepository
}

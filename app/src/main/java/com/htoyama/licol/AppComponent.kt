package com.htoyama.licol

import com.htoyama.licol.data.net.FavoriteService
import com.htoyama.licol.data.prefs.AppSetting
import com.htoyama.licol.data.sqlite.likedtweet.LikedTweetTableGateway
import com.htoyama.licol.domain.likedtweet.LikedTweetRepository
import com.htoyama.licol.domain.tag.TagRepository
import com.htoyama.licol.domain.user.UserRepository
import com.htoyama.licol.ui.auth.AuthActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface  AppComponent {
  fun inject(activity: AuthActivity)
  fun inject(activity: MainActivity)

  fun tweetTableGateway(): LikedTweetTableGateway
  fun appSetting(): AppSetting
  fun favoriteService(): FavoriteService
  fun likedRepository(): LikedTweetRepository
  fun tagRepository(): TagRepository
  fun userRepository(): UserRepository
}

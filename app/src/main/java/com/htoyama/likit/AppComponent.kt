package com.htoyama.likit

import com.htoyama.likit.data.common.net.FavoriteService
import com.htoyama.likit.data.prefs.AppSetting
import com.htoyama.likit.data.sqlite.tweet.TweetTableGateway
import com.htoyama.likit.domain.liked.LikedRepository
import com.htoyama.likit.domain.tag.TagRepository
import com.htoyama.likit.domain.user.UserRepository
import com.htoyama.likit.ui.auth.AuthActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface  AppComponent {
  fun inject(activity: AuthActivity)
  fun inject(activity: MainActivity)

  fun tweetTableGateway(): TweetTableGateway
  fun appSetting(): AppSetting
  fun favoriteService(): FavoriteService
  fun likedRepository(): LikedRepository
  fun tagRepository(): TagRepository
  fun userRepository(): UserRepository
}

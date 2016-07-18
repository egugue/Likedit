package com.htoyama.likit

import com.htoyama.likit.domain.liked.LikedRepository
import com.htoyama.likit.domain.tag.TagRepository
import com.htoyama.likit.ui.auth.AuthActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface  AppComponent {
  fun inject(activity: AuthActivity)
  fun inject(activity: MainActivity)

  fun likedRepository(): LikedRepository
  fun tagRepository(): TagRepository
}

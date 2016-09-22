package com.htoyama.likit.ui.tag.tweet.select

import android.content.Context
import com.htoyama.likit.AppComponent
import dagger.Component
import com.htoyama.likit.App

@TagTweetSelectScope
@Component(dependencies = arrayOf(AppComponent::class))
interface TagTweetSelectComponent {
  fun inject(activity: TagTweetSelectActivity)

  object Initializer {

    fun init(context: Context) = DaggerTagTweetSelectComponent.builder()
        .appComponent(App.component(context))
        .build()
  }

}
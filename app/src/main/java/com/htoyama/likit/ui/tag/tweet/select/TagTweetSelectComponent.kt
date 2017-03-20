package com.htoyama.likit.ui.tag.tweet.select

import com.htoyama.likit.AppComponent
import dagger.Component

@TagTweetSelectScope
@Component(dependencies = arrayOf(AppComponent::class))
interface TagTweetSelectComponent {
  fun inject(activity: TagTweetSelectActivity)
}
package com.htoyama.licol.ui.tag.tweet.select

import com.htoyama.licol.AppComponent
import dagger.Component

@TagTweetSelectScope
@Component(dependencies = arrayOf(AppComponent::class))
interface TagTweetSelectComponent {
  fun inject(activity: TagTweetSelectActivity)
}
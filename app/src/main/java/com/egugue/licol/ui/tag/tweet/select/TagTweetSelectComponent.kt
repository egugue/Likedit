package com.egugue.licol.ui.tag.tweet.select

import com.egugue.licol.AppComponent
import dagger.Component

@TagTweetSelectScope
@Component(dependencies = arrayOf(AppComponent::class))
interface TagTweetSelectComponent {
  fun inject(activity: TagTweetSelectActivity)
}
package com.egugue.licol.ui.usertweet

import com.egugue.licol.AppComponent
import com.egugue.licol.ui.ActivityScope
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class))
interface Component {
  fun inject(a: UserTweetActivity)
}

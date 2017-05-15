package com.egugue.licol.background.sync

import com.egugue.licol.AppComponent
import com.egugue.licol.background.SerivceScope
import dagger.Component

@SerivceScope
@Component(dependencies = arrayOf(AppComponent::class))
interface SyncComponent {
  fun inject(s: TweetSyncService)
}
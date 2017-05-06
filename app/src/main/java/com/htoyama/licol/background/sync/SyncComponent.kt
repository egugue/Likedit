package com.htoyama.licol.background.sync

import com.htoyama.licol.AppComponent
import com.htoyama.licol.background.SerivceScope
import dagger.Component

@SerivceScope
@Component(dependencies = arrayOf(AppComponent::class))
interface SyncComponent {
  fun inject(s: TweetSyncService)
}
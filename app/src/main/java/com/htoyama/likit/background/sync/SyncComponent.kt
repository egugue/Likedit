package com.htoyama.likit.background.sync

import com.htoyama.likit.AppComponent
import com.htoyama.likit.background.SerivceScope
import dagger.Component

@SerivceScope
@Component(dependencies = arrayOf(AppComponent::class))
interface SyncComponent {
  fun inject(s: TweetSyncService)
}
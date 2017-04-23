package com.htoyama.likit.background.sync

import com.htoyama.likit.AppComponent
import com.htoyama.likit.background.SerivceScope
import com.htoyama.likit.ui.home.HomeScope
import dagger.Component


@SerivceScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(SyncModule::class))
interface SyncComponent {
  fun inject(s: TweetSyncService)
}
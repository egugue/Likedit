package com.egugue.licol.ui.usertweet

import com.egugue.licol.AppComponent
import dagger.Component

@javax.inject.Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class Scope

@Scope
@Component(dependencies = arrayOf(AppComponent::class))
interface Component {
  fun inject(a: UserTweetActivity)
}

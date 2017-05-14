package com.egugue.licol.ui.home

import com.egugue.licol.AppComponent
import com.egugue.licol.ui.home.liked.HomeLikedFragment
import com.egugue.licol.ui.home.user.HomeUserFragment
import dagger.Component

/**
 * A Dagger component related to HomeScreen.
 */
@HomeScope
@Component(dependencies = arrayOf(AppComponent::class))
interface HomeComponent {
  fun inject(activity: HomeActivity)
  fun inject(likedFragment: HomeLikedFragment)
  fun inject(homeUserFragment: HomeUserFragment)
}
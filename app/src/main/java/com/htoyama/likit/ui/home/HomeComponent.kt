package com.htoyama.likit.ui.home

import com.htoyama.likit.old.AppComponent
import com.htoyama.likit.ui.home.liked.HomeLikedFragment
import com.htoyama.likit.ui.home.tag.HomeTagFragment
import dagger.Component

/**
 * A Dagger component related to HomeScreen.
 */
@HomeScope
@Component(dependencies = arrayOf(AppComponent::class))
interface HomeComponent {
  fun inject(activity: HomeActivity)
  fun inject(likedFragment: HomeLikedFragment)
  fun inject(tagFragment: HomeTagFragment)
}
package com.htoyama.likit.ui.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.htoyama.likit.ui.home.liked.HomeLikedFragment
import com.htoyama.likit.ui.home.tag.HomeTagFragment

/**
 * A [FragmentPagerAdapter] attatched by HomeActivity
 */
internal class HomePagerAdapter(fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

  override fun getItem(position: Int): Fragment? {
    return when(position) {
      0 -> HomeTagFragment.new()
      1 -> HomeLikedFragment.new()
      else -> throw IllegalStateException("Not excepted position: $position")
    }
  }

  override fun getCount(): Int {
    return 2
  }

}

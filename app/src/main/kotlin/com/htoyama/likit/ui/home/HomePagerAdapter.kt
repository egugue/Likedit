package com.htoyama.likit.ui.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.htoyama.likit.ui.home.liked.HomeLikedFragment
import com.htoyama.likit.ui.home.tag.HomeTagFragment

/**
 * A [FragmentPagerAdapter] attatched by HomeActivity
 */
internal class HomePagerAdapter(fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

  override fun getItem(position: Int): Fragment? {
    return Page.of(position)
        .getFragment()
  }

  override fun getCount(): Int {
    return Page.values().size
  }

  fun findFragmentByPosition(position: Int, pager: ViewPager): Fragment {
    return instantiateItem(pager, position) as Fragment
  }

}

package com.htoyama.licol.ui.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager

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

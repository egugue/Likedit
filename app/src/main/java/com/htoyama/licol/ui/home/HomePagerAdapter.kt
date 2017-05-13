package com.htoyama.licol.ui.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * A [FragmentPagerAdapter] attached by HomeActivity
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
}

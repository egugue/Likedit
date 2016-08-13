package com.htoyama.likit.ui.home

import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import com.htoyama.isGone
import com.htoyama.isVisible
import com.htoyama.likit.ui.home.liked.HomeLikedFragment

import com.htoyama.likit.ui.home.tag.HomeTagFragment
import com.htoyama.toGone
import com.htoyama.toVisible

/**
 * A class that describes a page inside of a [ViewPager]
 */
internal enum class Page {

  TAGS {
    override fun getFragment(): Fragment = HomeTagFragment.new();

    override fun manageFabSetting(fab: View, pager: ViewPager) {
      if (fab.isGone()) {
        fab.toVisible()
      }

      fab.setOnClickListener {
        val adapter = pager.adapter as HomePagerAdapter
        val fragment = adapter.findFragmentByPosition(ordinal, pager) as HomeTagFragment
        fragment.onClickFab()
      }
    }

  },

  LIKES {
    override fun getFragment(): Fragment = HomeLikedFragment.new();

    override fun manageFabSetting(fab: View, pager: ViewPager) {
      if (fab.isVisible()) {
        fab.toGone()
      }

      fab.setOnClickListener(null)
    }

  };

  abstract fun getFragment(): Fragment
  abstract fun manageFabSetting(fab: View, pager: ViewPager)

  companion object {

    fun of(position: Int): Page {
      return when(position) {
        TAGS.ordinal -> TAGS
        LIKES.ordinal -> LIKES
        else -> throw IllegalArgumentException("Invalid position $position")
      }
    }
  }


}

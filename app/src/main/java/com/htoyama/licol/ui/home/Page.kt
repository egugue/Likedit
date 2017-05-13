package com.htoyama.licol.ui.home

import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.htoyama.licol.ui.home.liked.HomeLikedFragment

import com.htoyama.licol.ui.home.user.HomeUserFragment

/**
 * A class representing a page inside of a [ViewPager]
 */
internal enum class Page {

  LIKES {
    override fun getFragment(): Fragment = HomeLikedFragment.new()
  },

  USER {
    override fun getFragment(): Fragment = HomeUserFragment.new()
  };

  abstract fun getFragment(): Fragment

  companion object {

    fun of(position: Int): Page {
      return when(position) {
        LIKES.ordinal -> LIKES
        USER.ordinal -> USER
        else -> throw IllegalArgumentException("Invalid position $position")
      }
    }
  }
}

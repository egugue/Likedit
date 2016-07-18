package com.htoyama.likit.ui.home

import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewPager
import com.htoyama.isGone
import com.htoyama.isVisible
import com.htoyama.toGone
import com.htoyama.toVisible

internal class FabVisibilityControllListener(private val fab: FloatingActionButton)
    : ViewPager.OnPageChangeListener {
  private var state: Int = -1

  override fun onPageScrollStateChanged(state: Int) {
    this.state = state
  }

  override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

  override fun onPageSelected(position: Int) {
    if (this.state == ViewPager.SCROLL_STATE_SETTLING) {
      if (position == 0 && fab.isGone()) {
        fab.toVisible()
      } else if (position == 1 && fab.isVisible()) {
        fab.toGone()
      }
    }
  }

}

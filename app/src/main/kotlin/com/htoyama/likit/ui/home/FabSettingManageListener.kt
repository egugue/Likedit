package com.htoyama.likit.ui.home

import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewPager

/**
 * A listener managing a [FloatingActionButton].
 */
internal class FabSettingManageListener(
    private val fab: FloatingActionButton,
    private val viewPager: ViewPager
) : ViewPager.OnPageChangeListener {

  private var state: Int = -1

  init {
    Page.TAGS
        .manageFabSetting(fab, viewPager)
  }

  override fun onPageScrollStateChanged(state: Int) {
    this.state = state
  }

  override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

  override fun onPageSelected(position: Int) {
    if (state != ViewPager.SCROLL_STATE_SETTLING) {
      return
    }

    Page.of(position)
        .manageFabSetting(fab, viewPager)
  }

}

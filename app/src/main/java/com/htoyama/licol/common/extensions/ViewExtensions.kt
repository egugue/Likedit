package com.htoyama.licol.common.extensions

import android.view.View
import android.view.View.*

/**
 * Created by toyamaosamuyu on 2016/06/20.
 */
fun View.toVisible() {
  this.visibility = VISIBLE
}

fun View.toGone() {
  this.visibility = GONE
}

fun View.isVisible(): Boolean = this.visibility == VISIBLE

fun View.isGone(): Boolean = this.visibility == GONE

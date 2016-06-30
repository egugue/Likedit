package com.htoyama.likit.util

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan

import com.twitter.sdk.android.tweetui.internal.HighlightedClickableSpan

abstract class ClickableLinkSpan(val bgColor: Int) : ClickableSpan(), HighlightedClickableSpan {
  private var isSelected: Boolean = false

  override fun updateDrawState(ds: TextPaint) {
    super.updateDrawState(ds)
    ds.bgColor = when(isSelected) {
      true -> bgColor
      false -> Color.TRANSPARENT
    }
  }

  override fun select(selected: Boolean) {
    isSelected = selected
  }

  override fun isSelected(): Boolean {
    return isSelected
  }
}

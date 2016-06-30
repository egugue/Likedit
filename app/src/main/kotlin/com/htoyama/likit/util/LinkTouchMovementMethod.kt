package com.htoyama.likit.util

import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.widget.TextView

import com.twitter.sdk.android.tweetui.internal.HighlightedClickableSpan

/**
 * A movement method that finds some [HighlightedClickableSpan]s in [TextView]
 * and calls [HighlightedClickableSpan.select] when the span is clicked and released.
 */
class LinkTouchMovementMethod private constructor()
    : LinkMovementMethod() {

  companion object {
    val instance: LinkTouchMovementMethod = LinkTouchMovementMethod()
  }

  private var mPressedSpan: HighlightedClickableSpan? = null

  override fun onTouchEvent(textView: TextView, spannable: Spannable,
      event: MotionEvent): Boolean {

    if (event.action == MotionEvent.ACTION_DOWN) {
      mPressedSpan = getSpan(textView, spannable, event)
      if (mPressedSpan != null) {
        mPressedSpan!!.select(true)
        Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan),
            spannable.getSpanEnd(mPressedSpan))
      }

    } else if (event.action == MotionEvent.ACTION_MOVE) {
      val touchedSpan = getSpan(textView, spannable, event)
      if (mPressedSpan != null && touchedSpan !== mPressedSpan) {
        mPressedSpan!!.select(false)
        mPressedSpan = null
        Selection.removeSelection(spannable)
      }

    } else {
      if (mPressedSpan != null) {
        mPressedSpan!!.select(false)
        super.onTouchEvent(textView, spannable, event)
      }
      mPressedSpan = null
      Selection.removeSelection(spannable)
    }

    return true
  }

  private fun getSpan(textView: TextView, spannable: Spannable, event: MotionEvent)
      : HighlightedClickableSpan? {
    var x = event.x.toInt()
    var y = event.y.toInt()
    x -= textView.totalPaddingLeft
    y -= textView.totalPaddingTop
    x += textView.scrollX
    y += textView.scrollY

    val layout = textView.layout
    val line = layout.getLineForVertical(y)
    val off = layout.getOffsetForHorizontal(line, x.toFloat())

    val link = spannable.getSpans(off, off, HighlightedClickableSpan::class.java)
    var span: HighlightedClickableSpan? = null
    if (link.size > 0) {
      span = link[0]
    }
    return span
  }
}

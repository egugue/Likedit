package com.htoyama.licol.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import com.htoyama.licol.R
import com.htoyama.licol.common.extensions.toGone
import com.htoyama.licol.common.extensions.toVisible

/**
 * A layout that manage each state such as Empty-State.
 */
class StateLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
  lateinit private var contentView: View
  lateinit private var progressView: View
  lateinit private var errorView: View
  private var emptyView: View? = null

  override fun onFinishInflate() {
    super.onFinishInflate()

    var progressStateIsAssigned = false
    for (i in 0..childCount - 1) {
      val view = getChildAt(i)
      val lp = view.layoutParams as LayoutParams
      val state = lp.state

      when (state) {
        STATE_CONTENT -> contentView = view
        STATE_ERROR -> errorView = view
        STATE_EMPTY -> emptyView = view
        STATE_PROGRESS -> {
          progressView = view
          progressStateIsAssigned = true
        }
        else -> throw IllegalStateException("Invalid layout_state attribute: " + state)
      }
    }

    if (!progressStateIsAssigned) {
      progressView = LayoutInflater.from(context).inflate(R.layout.default_progress, this, false)
      addView(progressView)
    }
  }

  override fun generateLayoutParams(attrs: AttributeSet): FrameLayout.LayoutParams {
    return LayoutParams(context, attrs)
  }

  override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
    return p is LayoutParams
  }

  /**
   * Show content.
   */
  fun showContent() {
    contentView.toVisible()
    progressView.toGone()
    errorView.toGone()
    emptyView?.toGone()
  }

  /**
   * Show progress.
   */
  fun showProgress() {
    contentView.toGone()
    progressView.toVisible()
    errorView.toGone()
    emptyView?.toGone()
  }

  /**
   * Show error.
   */
  fun showError() {
    contentView.toGone()
    progressView.toGone()
    errorView.toVisible()
    emptyView?.toGone()
  }

  /**
   * Show empty that is aka Empty-State
   */
  fun showEmptyState() {
    if (emptyView == null) {
      throw IllegalStateException("You should set empty_layout attribute.")
    }

    contentView.toGone()
    progressView.toGone()
    errorView.toGone()
    emptyView!!.toVisible()
  }

  class LayoutParams(c: Context, attrs: AttributeSet) : FrameLayout.LayoutParams(c, attrs) {
    var state = -1

    init {
      val a = c.obtainStyledAttributes(attrs, R.styleable.StateLayout_Layout)
      state = a.getInt(R.styleable.StateLayout_Layout_layout_state, -1)
      a.recycle()
    }

  }

  companion object {
    private val STATE_CONTENT = 0
    private val STATE_PROGRESS = 1
    private val STATE_ERROR = 2
    private val STATE_EMPTY = 3
  }
}
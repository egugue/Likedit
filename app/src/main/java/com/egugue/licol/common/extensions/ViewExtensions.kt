package com.egugue.licol.common.extensions

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.*
import com.egugue.licol.common.Irrelevant
import com.jakewharton.rxbinding2.support.v7.widget.scrollEvents
import io.reactivex.Observable

/**
 * Extensions for each [View]s
 */

fun View.toVisible() {
  this.visibility = VISIBLE
}

fun View.toGone() {
  this.visibility = GONE
}

fun View.isVisible(): Boolean = this.visibility == VISIBLE

fun View.isGone(): Boolean = this.visibility == GONE

/**
 * Add a [LoadMoreListener] which can notify the time when loading more
 */
fun RecyclerView.addOnLoadMoreListener(l: LoadMoreListener) = addOnScrollListener(
    object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (l.isLoading()) {
          return
        }
        if (l.hasLoadedItems()) {
          return
        }

        val manager = recyclerView.layoutManager as LinearLayoutManager
        val visibleCount = recyclerView.childCount
        val totalCount = manager.itemCount
        val firstPos = manager.findFirstVisibleItemPosition()
        if (totalCount - visibleCount <= firstPos) {
          l.onLoadMore()
        }
      }
    }
)

/**
 * @see addOnLoadMoreListener
 */
interface LoadMoreListener {
  /** Invoked when it should load more items */
  fun onLoadMore()

  /** Whether it is loading now or not */
  fun isLoading(): Boolean

  /** Whether it has loaded all items or not */
  fun hasLoadedItems(): Boolean
}

/**
 * Create an Observable which emits when a subscriber should load more.
 */
fun RecyclerView.loadMoreEvent(predicate: LoadMorePredicate): Observable<Any> {
  if (layoutManager !is LinearLayoutManager) {
    throw IllegalStateException(
        "layout manager must be LinearLayoutManager, but was ${layoutManager.javaClass.simpleName}")
  }

  return this.scrollEvents()
      .filter {
        if (predicate.isLoading() || predicate.hasLoadedItems()) {
          false
        } else {
          val manager = layoutManager as LinearLayoutManager
          val visibleCount = childCount
          val totalCount = manager.itemCount
          val firstPos = manager.findFirstVisibleItemPosition()
          totalCount - visibleCount <= firstPos
        }
      }
      .map { Irrelevant.get() }
}

/**
 * @see loadMoreEvent
 */
interface LoadMorePredicate {
  /** Whether it has loaded all items or not */
  fun hasLoadedItems(): Boolean

  /** Whether it is loading now or not */
  fun isLoading(): Boolean
}

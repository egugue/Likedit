package com.egugue.licol.ui.search

import com.egugue.licol.application.search.SearchAppService
import com.egugue.licol.application.search.Suggestions
import com.egugue.licol.common.extensions.observeOnMain
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit


/**
 * Convert a source into an action that user inputs a query
 */
internal fun Observable<TextViewAfterTextChangeEvent>.textChangeAction(): Observable<String> {
  return this
      .throttleLast(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
      .map { event -> event.editable()!!.toString() }
      .distinctUntilChanged()
}

/**
 * Convert a source into a sink which emits [Suggestions]
 */
internal fun Observable<String>.toSuggestions(service: SearchAppService): Observable<Suggestions> {
  return this.flatMap { query ->
    if (query.length <= 2) {
      Observable.just(Suggestions.empty())
    } else {
      service.getSearchSuggestions(query)
          .doOnError { Timber.e(it) }
          .onErrorReturn { Suggestions.empty() }
          .observeOnMain()
    }
  }
}

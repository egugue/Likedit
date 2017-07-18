package com.egugue.licol.ui.search

import com.egugue.licol.application.search.SearchAppService
import com.egugue.licol.application.search.Suggestions
import com.egugue.licol.common.extensions.observeOnMain
import com.jakewharton.rxbinding2.widget.SearchViewQueryTextEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit


/**
 * Convert a source into an action that the user is inputting a query
 */
fun Observable<SearchViewQueryTextEvent>.toQueryChangingAction(): Observable<String> {
  return this
      .filter { !it.isSubmitted }
      .map { it.queryText().toString() }
      .throttleLast(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
      .distinctUntilChanged()
}

/**
 * Convert a source into an action that the user submitted a query
 */
fun Observable<SearchViewQueryTextEvent>.toQuerySubmittedAction(): Observable<String> {
  return this
      .filter { it.isSubmitted }
      .map { it.queryText().toString() }
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

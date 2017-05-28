package com.egugue.licol.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import butterknife.bindView
import com.egugue.licol.R
import com.egugue.licol.application.search.SearchAppService
import com.egugue.licol.application.search.Suggestions
import com.egugue.licol.common.extensions.observeOnMain
import com.egugue.licol.ui.common.activity.BaseRxActivity
import com.egugue.licol.ui.common.recyclerview.DividerItemDecoration
import com.jakewharton.rxbinding2.widget.RxTextView
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchActivity : BaseRxActivity() {

  companion object {

    fun createIntent(context: Context): Intent {
      return Intent(context, SearchActivity::class.java)
    }
  }

  private val toolbar: Toolbar by bindView(R.id.toolbar)
  private val listView: RecyclerView by bindView(R.id.search_assist_list)
  private val searchQueryView: EditText by bindView(R.id.search_query)
  @Inject lateinit var searchAppService: SearchAppService
  @Inject lateinit var listController: SuggestionListController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_search)

    SearchComponent.Initializer
        .init(this)
        .inject(this)

    initToolbar()
    initList()
    initSearchEditText()
  }

  private fun initToolbar() {
    setSupportActionBar(toolbar)

    val bar = supportActionBar
    if (bar != null) {
      bar.setDisplayHomeAsUpEnabled(true)
      bar.setDisplayShowHomeEnabled(true)
      bar.setDisplayShowTitleEnabled(false)
      bar.setHomeButtonEnabled(true)
    }
  }

  private fun initList() {
    listView.adapter = listController.adapter
    listView.layoutManager = LinearLayoutManager(this)
    listView.addItemDecoration(DividerItemDecoration(this))
  }

  private fun initSearchEditText() {
    // When user input search query.
    val query = RxTextView.afterTextChangeEvents(searchQueryView)
        .bindToLifecycle(this)
        .throttleLast(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
        .map { event -> event.editable()!!.toString() }
        .distinctUntilChanged()

    query.toSuggestions()
        .subscribe {
          listController.replaceWith(it)
          listController.requestModelBuild()
        }

    // When user input search button.
    RxTextView.editorActionEvents(searchQueryView)
        .filter { event -> event.actionId() == EditorInfo.IME_ACTION_SEARCH }
        .bindToLifecycle(this)
        .subscribe { searchEvent ->
          val v = searchEvent.view()

          val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
          imm.hideSoftInputFromWindow(v.windowToken, 0)

          val query = v.text.toString()
          Timber.d("ーーー", query)
        }
  }

  private fun Observable<String>.toSuggestions(): Observable<Suggestions> = compose({
    it.flatMap { query ->
      if (query.length <= 2) {
        Observable.just(Suggestions.empty())
      } else {
        searchAppService.getSearchSuggestions(query)
            .doOnError { Timber.e(it) }
            .onErrorReturn { Suggestions.empty() }
            .observeOnMain()
      }
    }
  })
}

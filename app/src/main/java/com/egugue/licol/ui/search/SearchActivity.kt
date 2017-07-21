package com.egugue.licol.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import butterknife.BindView
import butterknife.ButterKnife
import com.egugue.licol.R
import com.egugue.licol.application.search.SearchAppService
import com.egugue.licol.common.extensions.toast
import com.egugue.licol.ui.common.base.BaseActivity
import com.egugue.licol.ui.search.suggestion.SuggestionLayout
import com.egugue.licol.ui.usertweet.UserTweetActivity
import com.jakewharton.rxbinding2.widget.RxSearchView
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import javax.inject.Inject


class SearchActivity : BaseActivity() {

  companion object {

    fun createIntent(context: Context): Intent {
      return Intent(context, SearchActivity::class.java)
    }
  }

  @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
  @BindView(R.id.search_suggestions) lateinit var suggestionLayout: SuggestionLayout
  @BindView(R.id.search_query) lateinit var searchQueryView: SearchView

  @Inject lateinit var searchAppService: SearchAppService

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.search_activity)
    ButterKnife.bind(this)

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
    suggestionLayout.itemClickListenr = {
      startActivity(UserTweetActivity.createIntent(this, it))
    }
  }

  private fun initSearchEditText() {
    // When user input search query.
    val queryEvent = RxSearchView.queryTextChangeEvents(searchQueryView).share()

    queryEvent
        .toQueryChangingAction()
        .toSuggestions(searchAppService)
        .bindToLifecycle(this)
        .subscribe {
          suggestionLayout.set(it)
        }

    queryEvent
        .toQuerySubmittedAction()
        .bindToLifecycle(this)
        .subscribe {
          val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
          imm.hideSoftInputFromWindow(searchQueryView.windowToken, 0)

          //TODO
          toast("$it is submitted")
        }

    //TODO: Instead, create a custom SearchView because of flaky code
    val editFrame = searchQueryView.findViewById<View>(
        resources.getIdentifier("android:id/search_edit_frame", null, null))
    if (editFrame != null) {
      val lp = editFrame.layoutParams as ViewGroup.MarginLayoutParams
      lp.leftMargin = 0
      editFrame.layoutParams = lp
    }

    val plate = searchQueryView.findViewById<View>(
        resources.getIdentifier("android:id/search_plate", null, null))
    plate?.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
  }
}

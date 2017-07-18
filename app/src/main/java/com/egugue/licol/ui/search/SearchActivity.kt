package com.egugue.licol.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import butterknife.BindView
import butterknife.ButterKnife
import com.egugue.licol.R
import com.egugue.licol.application.search.SearchAppService
import com.egugue.licol.common.extensions.toast
import com.egugue.licol.ui.common.base.BaseActivity
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
  @BindView(R.id.search_assist_list) lateinit var listView: RecyclerView
  @BindView(R.id.search_query) lateinit var searchQueryView: SearchView

  @Inject lateinit var searchAppService: SearchAppService
  @Inject lateinit var listController: SuggestionListController

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
    val layoutManager = LinearLayoutManager(this)
    listView.adapter = listController.adapter
    listView.layoutManager = layoutManager
    listView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
    listController.userClickListener = {
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
          listController.replaceWith(it)
          listController.requestModelBuild()
        }

    queryEvent
        .toQuerySubmittedAction()
        .bindToLifecycle(this)
        .subscribe {
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

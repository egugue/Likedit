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
import butterknife.BindView
import butterknife.ButterKnife
import com.egugue.licol.R
import com.egugue.licol.application.search.SearchAppService
import com.egugue.licol.ui.common.base.BaseActivity
import com.egugue.licol.ui.common.recyclerview.DividerItemDecoration
import com.egugue.licol.ui.usertweet.UserTweetActivity
import com.jakewharton.rxbinding2.widget.RxTextView
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
  @BindView(R.id.search_query) lateinit var searchQueryView: EditText

  @Inject lateinit var searchAppService: SearchAppService
  @Inject lateinit var listController: SuggestionListController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_search)
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
    listView.adapter = listController.adapter
    listView.layoutManager = LinearLayoutManager(this)
    listView.addItemDecoration(DividerItemDecoration(this))
    listController.userClickListener = {
      startActivity(UserTweetActivity.createIntent(this, it))
    }
  }

  private fun initSearchEditText() {
    // When user input search query.
    val query = RxTextView.afterTextChangeEvents(searchQueryView)

    query
        .textChangeAction()
        .toSuggestions(searchAppService)
        .bindToLifecycle(this)
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
        }
  }
}

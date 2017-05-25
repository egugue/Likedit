package com.egugue.licol.ui.search

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager

import com.egugue.licol.R
import com.egugue.licol.databinding.ActivitySearchBinding
import com.egugue.licol.domain.tag.Tag
import com.egugue.licol.domain.user.User
import com.egugue.licol.ui.common.activity.BaseRxActivity
import com.jakewharton.rxbinding2.widget.RxTextView

import java.util.concurrent.TimeUnit

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers

import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import timber.log.Timber

class SearchActivity : BaseRxActivity(), Presenter.View, AssistAdapter.OnItemClickListener {

  companion object {

    fun createIntent(context: Context): Intent {
      return Intent(context, SearchActivity::class.java)
    }
  }

  @Inject internal lateinit var presenter: Presenter
  lateinit private var binding: ActivitySearchBinding
  lateinit private var adapter: AssistAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView<ActivitySearchBinding>(this, R.layout.activity_search)

    SearchComponent.Initializer
        .init(this)
        .inject(this)

    initToolbar()
    initList()
    initSearchEditText()
    presenter.setView(this)
  }

  override fun onDestroy() {
    presenter.dispose()
    super.onDestroy()
  }

  override fun showAssist(assist: Assist) {
    adapter.setAssist(assist)
  }

  override fun onTagClick(tag: Tag) {
    Timber.d("OnTagClick. Tag = " + tag.toString())
  }

  override fun onUserClick(user: User) {
    Timber.d("OnUserClick. User = " + user.toString())
  }

  private fun initToolbar() {
    setSupportActionBar(binding.toolbar)

    val bar = supportActionBar
    if (bar != null) {
      bar.setDisplayHomeAsUpEnabled(true)
      bar.setDisplayShowHomeEnabled(true)
      bar.setDisplayShowTitleEnabled(false)
      bar.setHomeButtonEnabled(true)
    }
  }

  private fun initList() {
    val listView = binding.searchAssistList
    adapter = AssistAdapter(this)
    listView.adapter = adapter
    listView.layoutManager = LinearLayoutManager(this)
  }

  private fun initSearchEditText() {
    val searchView = binding.searchQuery

    // When user input search query.
    RxTextView.afterTextChangeEvents(searchView)
        .bindToLifecycle(this)
        .throttleLast(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
        .map<String> { event -> event.editable()!!.toString() }
        .distinctUntilChanged()
        .subscribe { query ->
          val length = query.length

          if (length == 0) {
            adapter.setAssist(Assist.empty())
          } else if (length >= 3) {
            presenter.loadAssist(query)
          }
        }

    // When user input search button.
    RxTextView.editorActionEvents(searchView)
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
}
package com.egugue.licol.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.egugue.licol.R
import com.egugue.licol.application.search.SearchAppService
import com.egugue.licol.common.extensions.color
import com.egugue.licol.common.extensions.toGone
import com.egugue.licol.common.extensions.toVisible
import com.egugue.licol.ui.common.IcePick
import com.egugue.licol.ui.common.IcePickDelegate
import com.egugue.licol.ui.common.base.BaseActivity
import com.egugue.licol.ui.search.result.SearchResultFragment
import com.egugue.licol.ui.search.suggestion.SuggestionListController
import com.egugue.licol.ui.usertweet.UserTweetActivity
import com.jakewharton.rxbinding2.widget.RxSearchView
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.search_activity.app_bar
import kotlinx.android.synthetic.main.search_activity.search_query
import kotlinx.android.synthetic.main.search_activity.search_suggestion_list
import kotlinx.android.synthetic.main.search_activity.toolbar
import kotlinx.android.synthetic.main.search_activity.whole_layout
import javax.inject.Inject


/**
 * TODO: Hold view-states and restore them when rotating or something like it
 */
class SearchActivity : BaseActivity(), IcePick by IcePickDelegate() {

  companion object {

    fun createIntent(context: Context): Intent {
      return Intent(context, SearchActivity::class.java)
    }
  }

  private val suggestionListController: SuggestionListController = SuggestionListController()

  val component: SearchComponent by lazy { SearchComponent.Initializer.init(this) }
  @Inject lateinit var searchAppService: SearchAppService

  //FIXME
  val appBar: CardView by lazy { app_bar }
  private var wasSuggestionShown: Boolean by state(true)

  override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
    super.onRestoreInstanceState(savedInstanceState)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.search_activity)
    component.inject(this)
    restoreInstanceState(savedInstanceState)

    initToolbar()
    initSuggestionList()
    initSearchEditText()

    when (wasSuggestionShown) {
      true -> showSuggestionAndHideResult()
      else -> hideSuggestionAndShowResult("")
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    wasSuggestionShown = search_suggestion_list.visibility == View.VISIBLE
    saveInstanceState(outState)
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

  private fun initSuggestionList() {
    val layoutManager = LinearLayoutManager(this)
    search_suggestion_list.adapter = suggestionListController.adapter
    search_suggestion_list.layoutManager = layoutManager
    search_suggestion_list.itemAnimator = null
    search_suggestion_list.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
    suggestionListController.userClickListener = {
      startActivity(UserTweetActivity.createIntent(this, it))
    }
  }

  private fun initSearchEditText() {
    // When user input search query.
    search_query.requestFocus()

    //TODO: RxBinding
    search_query.setOnQueryTextFocusChangeListener { _, isFocusing ->
      if (isFocusing) {
        showSuggestionAndHideResult()
      }
    }

    val queryEvent = RxSearchView.queryTextChangeEvents(search_query).share()
    queryEvent
        .toQueryChangingAction()
        .toSuggestions(searchAppService)
        .bindToLifecycle(this)
        .subscribe {
          //suggestionLayout.set(it)
          suggestionListController.replaceWith(it)
          suggestionListController.requestModelBuild()
          search_suggestion_list.scrollToPosition(0)
        }

    queryEvent
        .toQuerySubmittedAction()
        .bindToLifecycle(this)
        .subscribe {
          val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
          imm.hideSoftInputFromWindow(search_query.windowToken, 0)

          hideSuggestionAndShowResult(it)
        }

    //TODO: Instead, create a custom SearchView because of flaky code
    // remove marginLeft to close toolbar's up button
    val editFrame = search_query.findViewById<View>(
        resources.getIdentifier("android:id/search_edit_frame", null, null))
    if (editFrame != null) {
      val lp = editFrame.layoutParams as ViewGroup.MarginLayoutParams
      lp.leftMargin = 0
      editFrame.layoutParams = lp
    }

    // remove the underline in EditText of SearchView
    val plate = search_query.findViewById<View>(
        resources.getIdentifier("android:id/search_plate", null, null))
    plate?.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
  }

  private fun hideSuggestionAndShowResult(query: String) {
    search_query.clearFocus()
    search_suggestion_list.toGone()
    whole_layout.setBackgroundColor(color(R.color.light_background))

    val f = supportFragmentManager.findFragmentById(R.id.fragment_container)
    if (f == null) {
      supportFragmentManager.beginTransaction()
          .replace(R.id.fragment_container, SearchResultFragment.new(query))
          .commit()
    }
  }

  private fun showSuggestionAndHideResult() {
    search_query.requestFocus()
    search_suggestion_list.toVisible()
    whole_layout.setBackgroundColor(color(R.color.search_screen_background))

    val f = supportFragmentManager.findFragmentById(R.id.fragment_container)
    if (f != null) {
      supportFragmentManager.beginTransaction()
          .remove(f)
          .commit()
    }
  }
}

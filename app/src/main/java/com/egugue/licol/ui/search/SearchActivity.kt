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
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import butterknife.BindView
import butterknife.ButterKnife
import com.egugue.licol.R
import com.egugue.licol.application.search.SearchAppService
import com.egugue.licol.common.extensions.color
import com.egugue.licol.common.extensions.toGone
import com.egugue.licol.common.extensions.toVisible
import com.egugue.licol.ui.common.base.BaseActivity
import com.egugue.licol.ui.search.result.SearchResultFragment
import com.egugue.licol.ui.search.suggestion.SuggestionListController
import com.egugue.licol.ui.usertweet.UserTweetActivity
import com.jakewharton.rxbinding2.widget.RxSearchView
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import javax.inject.Inject


/**
 * TODO: Hold view-states and restore them when rotating or something like it
 */
class SearchActivity : BaseActivity() {

  companion object {

    fun createIntent(context: Context): Intent {
      return Intent(context, SearchActivity::class.java)
    }
  }

  @BindView(R.id.whole_layout) lateinit var wholeLayout: ViewGroup
  @BindView(R.id.search_panel) lateinit var searchPanel: View
  @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
  @BindView(R.id.search_query) lateinit var searchQueryView: SearchView
  @BindView(R.id.search_suggestion_list) lateinit var suggestionRecyclerView: RecyclerView
  @BindView(R.id.fragment_container) lateinit var fragmentContainer: ViewGroup
  private val suggestionListController: SuggestionListController = SuggestionListController()

  val component: SearchComponent by lazy { SearchComponent.Initializer.init(this) }
  @Inject lateinit var searchAppService: SearchAppService

  private var initialAppbarHeight: Int = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.search_activity)
    ButterKnife.bind(this)

    component.inject(this)

    initToolbar()
    initSuggestionList()
    initSearchEditText()
    showSuggestionAndHideResult()

    searchPanel.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
      override fun onGlobalLayout() {
        initialAppbarHeight = searchPanel.height
        searchPanel.viewTreeObserver.removeOnGlobalLayoutListener(this)
      }
    })
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
    suggestionRecyclerView.adapter = suggestionListController.adapter
    suggestionRecyclerView.layoutManager = layoutManager
    suggestionRecyclerView.itemAnimator = null
    suggestionRecyclerView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
    suggestionListController.userClickListener = {
      startActivity(UserTweetActivity.createIntent(this, it))
    }

    /*
    //suggestionLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    suggestionLayout.itemClickListenr = {
      startActivity(UserTweetActivity.createIntent(this, it))
    }
    */
  }

  private fun initSearchEditText() {
    // When user input search query.
    searchQueryView.requestFocus()

    //TODO: RxBinding
    searchQueryView.setOnQueryTextFocusChangeListener { _, isFocusing ->
      if (isFocusing) {
        showSuggestionAndHideResult()
      }
    }

    val queryEvent = RxSearchView.queryTextChangeEvents(searchQueryView).share()
    queryEvent
        .toQueryChangingAction()
        .toSuggestions(searchAppService)
        .bindToLifecycle(this)
        .subscribe {
          //suggestionLayout.set(it)
          suggestionListController.replaceWith(it)
          suggestionListController.requestModelBuild()
          suggestionRecyclerView.scrollToPosition(0)
        }

    queryEvent
        .toQuerySubmittedAction()
        .bindToLifecycle(this)
        .subscribe {
          val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
          imm.hideSoftInputFromWindow(searchQueryView.windowToken, 0)

          hideSuggestionAndShowResult(it)
        }

    //TODO: Instead, create a custom SearchView because of flaky code
    // remove marginLeft to close toolbar's up button
    val editFrame = searchQueryView.findViewById<View>(
        resources.getIdentifier("android:id/search_edit_frame", null, null))
    if (editFrame != null) {
      val lp = editFrame.layoutParams as ViewGroup.MarginLayoutParams
      lp.leftMargin = 0
      editFrame.layoutParams = lp
    }

    // remove the underline in EditText of SearchView
    val plate = searchQueryView.findViewById<View>(
        resources.getIdentifier("android:id/search_plate", null, null))
    plate?.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
  }

  private fun hideSuggestionAndShowResult(query: String) {
    searchQueryView.clearFocus()
    suggestionRecyclerView.toGone()

    supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, SearchResultFragment.new(query, initialAppbarHeight))
        .commitNow()

    wholeLayout.setBackgroundColor(color(R.color.light_background))
  }

  private fun showSuggestionAndHideResult() {
    searchQueryView.requestFocus()
    suggestionRecyclerView.toVisible()

    val f = supportFragmentManager.findFragmentById(R.id.fragment_container)
    if (f != null) {
      supportFragmentManager.beginTransaction()
          .remove(f)
          .commit()
    }

    wholeLayout.setBackgroundColor(color(R.color.search_screen_background))
  }
}

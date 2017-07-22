package com.egugue.licol.ui.search.result

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.egugue.licol.R
import com.egugue.licol.application.search.SearchAppService
import com.egugue.licol.common.extensions.observeOnMain
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.tweet.media.Photo
import com.egugue.licol.domain.user.User
import com.egugue.licol.ui.common.StateLayout
import com.egugue.licol.ui.home.liked.LikedTweetListController
import com.egugue.licol.ui.search.SearchActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import javax.inject.Inject

/**
 * A fragment which shows search results
 */
class SearchResultFragment : RxFragment() {

  companion object {

    fun new(query: String, appbarHeight: Int): SearchResultFragment {
      val f = SearchResultFragment()
      val b = Bundle()
      b.putString("query", query)
      b.putInt("appbarHeight", appbarHeight)
      f.arguments = b
      return f
    }
  }

  @Inject lateinit var searchAppService: SearchAppService

  @BindView(R.id.search_result_list) lateinit var recyclerView: RecyclerView
  @BindView(R.id.search_result_state_layout) lateinit var stateLayout: StateLayout
  @BindView(R.id.search_result_error_state) lateinit var errorView: TextView
  lateinit private var unbinder: Unbinder

  private val listController = LikedTweetListController()
  private val query: String by lazy { arguments.getString("query") }
  private val appbarHeight: Int by lazy { arguments.getInt("appbarHeight") }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    (context as SearchActivity).component.inject(this)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.search_result_fragment, container, false)
    unbinder = ButterKnife.bind(this, view)
    return view
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    initSearchResultList()

    stateLayout.showProgress()
    searchAppService.getSearchResult(query, 1, 30)
        .observeOnMain()
        .subscribe {
          stateLayout.showContent()
          listController.setData(it)
          listController.requestModelBuild()
        }
  }

  private fun initSearchResultList() {
    val layoutManager = LinearLayoutManager(context)
    //recyclerView.setPadding(0, listPaddingTop, 0, 0)
    recyclerView.setPadding(recyclerView.paddingLeft, recyclerView.paddingTop + appbarHeight,
        recyclerView.paddingRight, recyclerView.paddingBottom)

    recyclerView.adapter = listController.adapter
    recyclerView.layoutManager = layoutManager
    recyclerView.itemAnimator = null
    recyclerView.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

    listController.callbacks = object : LikedTweetListController.AdapterCallbacks {
      override fun onTweetLinkClicked(url: String) {
      }

      override fun onWholeTweetClicked(likedTweet: LikedTweet, user: User) {
      }

      override fun onTweetUserAvatarClicked(user: User) {
      }

      override fun onTweetPhotoClicked(photo: Photo) {
      }
    }
  }

}
package com.egugue.licol.ui.search.result

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnPreDrawListener
import com.egugue.licol.R
import com.egugue.licol.application.search.SearchAppService
import com.egugue.licol.common.extensions.observeOnMain
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.tweet.media.Photo
import com.egugue.licol.domain.user.User
import com.egugue.licol.ui.home.liked.LikedTweetListController
import com.egugue.licol.ui.search.SearchActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import kotlinx.android.synthetic.main.search_result_fragment.search_result_list
import kotlinx.android.synthetic.main.search_result_fragment.state_layout
import javax.inject.Inject

/**
 * A fragment which shows search results
 */
class SearchResultFragment : RxFragment() {

  companion object {

    fun new(query: String): SearchResultFragment {
      val f = SearchResultFragment()
      val b = Bundle()
      b.putString("query", query)
      f.arguments = b
      return f
    }
  }

  @Inject lateinit var searchAppService: SearchAppService

  private val listController = LikedTweetListController()
  private val query: String by lazy { arguments.getString("query") }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    (context as SearchActivity).component.inject(this)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View?
      = inflater.inflate(R.layout.search_result_fragment, container, false)

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    initSearchResultList()

    state_layout.showProgress()
    searchAppService.getSearchResult(query, 1, 30)
        .observeOnMain()
        .subscribe {
          state_layout.showContent()
          listController.setData(it)
          listController.requestModelBuild()
        }
  }

  private fun initSearchResultList() {
    //FIXME: depends on the specific activity directly
    val recyclerView = search_result_list

    val appBar = (activity as SearchActivity).appBar
    appBar.viewTreeObserver.addOnPreDrawListener(object : OnPreDrawListener {
      override fun onPreDraw(): Boolean {
        appBar.viewTreeObserver.removeOnPreDrawListener(this)

        val appbarHeight = appBar.height
        recyclerView.setPadding(recyclerView.paddingLeft, recyclerView.paddingTop + appbarHeight,
            recyclerView.paddingRight, recyclerView.paddingBottom)
        return true
      }
    })

    val layoutManager = LinearLayoutManager(context)
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
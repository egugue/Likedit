package com.htoyama.likit.ui.home.liked

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.htoyama.likit.R
import com.htoyama.likit.domain.liked.LikedRepository
import com.htoyama.likit.domain.liked.LikedTweet
import com.htoyama.likit.ui.TweetAdapter
import com.htoyama.likit.ui.home.HomeActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 *
 */
class HomeLikedFragment : Fragment() {

  companion object {
    fun new(): HomeLikedFragment = HomeLikedFragment()
  }

  lateinit private var listView: RecyclerView
  @Inject lateinit var likedRepisitory: LikedRepository
  private val adapter: TweetAdapter = TweetAdapter()

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    (activity as HomeActivity)
        .component
        .inject(this)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    listView = inflater!!.inflate(R.layout.fragment_home_liked, container, false) as RecyclerView
    return listView
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    listView.adapter = adapter
    listView.layoutManager = LinearLayoutManager(activity)
    likedRepisitory.find(1, 200)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { likedList ->
              adapter.setTweetList(likedList.map(LikedTweet::tweet))
            },
            Throwable::printStackTrace
        )
  }
}
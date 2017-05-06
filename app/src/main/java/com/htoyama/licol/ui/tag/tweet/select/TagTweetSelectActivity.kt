package com.htoyama.licol.ui.tag.tweet.select

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import butterknife.bindView
import com.htoyama.licol.App

import com.htoyama.licol.R
import com.htoyama.licol.domain.likedtweet.LikedTweet
import com.htoyama.licol.domain.tag.Tag
import com.htoyama.licol.ui.common.DividerItemDecoration
import com.htoyama.licol.ui.common.StateLayout
import com.htoyama.licol.ui.common.activity.BaseActivity
import javax.inject.Inject

class TagTweetSelectActivity : BaseActivity(), Presenter.View {

  companion object {
    private val TAG_ID: String = "tag_id"
    private val TAG_NAME: String = "tag_name"

    fun createIntent(context: Context, tag: Tag): Intent {
      val intent = Intent(context, TagTweetSelectActivity::class.java)
      intent.putExtra(TAG_ID, tag.id)
      intent.putExtra(TAG_NAME, tag.name)
      return intent
    }
  }

  private val toolbar: Toolbar by bindView(R.id.toolbar)
  private val fab: FloatingActionButton by bindView(R.id.fab)
  private val stateLayout: StateLayout by bindView(R.id.tag_tweet_select_state_layout)
  private val listView: RecyclerView by bindView(R.id.tag_tweet_select_list)
  private val adapter: ListAdapter = ListAdapter()
  private val component: TagTweetSelectComponent by lazy {
    DaggerTagTweetSelectComponent.builder()
            .appComponent(App.component(this))
            .build()
  }

  @Inject lateinit internal var presenter: Presenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_tag_tweet_select)
    toolbar.title = getTagName()
    setSupportActionBar(toolbar)

    component.inject(this)

    fab.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }

    listView.adapter = adapter
    listView.layoutManager = LinearLayoutManager(this)
    listView.addItemDecoration(DividerItemDecoration(this))
  }

  override fun onStart() {
    super.onStart()
    presenter.setView(this)
    presenter.loadNextLikedList()
  }

  override fun onStop() {
    presenter.dispose()
    super.onStop()
  }

  override fun showProgress() {
    stateLayout.showProgress()
  }

  override fun showNextLikedList(next: List<LikedTweet>) {
    stateLayout.showContent()
    adapter.addItemList(next)
  }

  private fun getTagId(): Long {
    return intent.extras.getLong(TAG_ID)
  }

  private fun getTagName(): String {
    return intent.extras.getString(TAG_NAME)
  }

}

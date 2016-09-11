package com.htoyama.likit.ui.tag.tweet.select

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import butterknife.bindView

import com.htoyama.likit.R
import com.htoyama.likit.common.extensions.toast
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.ui.common.activity.BaseActivity

class TagTweetSelectActivity : BaseActivity() {

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

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_tag_tweet_select)
    toolbar.title = getTagName()
    setSupportActionBar(toolbar)

    val fab = findViewById(R.id.fab) as FloatingActionButton?
    fab!!.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }

    toast(getTagId().toString())
  }

  private fun getTagId(): Long {
    return intent.extras.getLong(TAG_ID)
  }

  private fun getTagName(): String {
    return intent.extras.getString(TAG_NAME)
  }

}

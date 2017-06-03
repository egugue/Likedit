package com.egugue.licol.ui.usertweet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import butterknife.bindView
import com.egugue.licol.R
import com.egugue.licol.domain.user.User

/**
 * An activity which shows liked tweets of a specific user.
 */
class UserTweetActivity : AppCompatActivity() {

  companion object {

    /**
     * Create an intent with given args
     */
    fun createIntent(context: Context, user: User): Intent {
      val intent = Intent(context, UserTweetActivity::class.java)
      intent.putExtra("userId", user.id)
      intent.putExtra("userName", user.name)
      return intent
    }
  }

  private val titleView: TextView by bindView(R.id.title)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.user_tweet_activity)

    val userName = intent.extras.getString("userName")
    titleView.text = userName
  }
}

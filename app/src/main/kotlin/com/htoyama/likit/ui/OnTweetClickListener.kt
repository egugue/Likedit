package com.htoyama.likit.ui

/**
 * Interface definition for a callback to be invoked when a view displaying a tweet is clicked.
 */
interface OnTweetClickListener {

  /**
   * Called when a url in tweet has been clicked.
   */
  fun onUrlClicked(url: String)
}
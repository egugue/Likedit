package com.htoyama.likit.ui

import android.util.Log
import twitter4j.Status

/**
 * Created by toyamaosamuyu on 2016/06/14.
 */
object TwitterLogger {

  fun log(status: Status) {
    Log.d("---", " ")
    Log.d("---", " ")
    Log.d("---", status.id.toString())
    Log.d("---", status.user.name)
    Log.d("----", "inReplyToUserId: " + status.id)
    Log.d("------", "created at: " + status.createdAt.toString())
    Log.d("------", "isTruncated: " + status.isTruncated.toString())
    Log.d("------", "text: " + status.text)
    Log.d("------", "source: " + status.source)
    Log.d("------", "reTweetCount: " + status.retweetCount.toString())
    Log.d("------", "inReplyToScreenName: " + status.inReplyToScreenName)
    Log.d("------", "inReplyToStatusId: " + status.inReplyToStatusId)
    Log.d("------", "inReplyToUserId: " + status.inReplyToUserId)

    Log.d("------", "--- Symbol entities ----")
    for (entity in status.symbolEntities) {
      Log.d("--------", "url: " + entity.text)
      Log.d("--------", "-----")
    }

    Log.d("------", "--- Url Entities ----")
    for (entity in status.urlEntities) {
      Log.d("--------", "url: " + entity.url)
      Log.d("--------", "displayURL: " + entity.displayURL)
      Log.d("--------", "expandedURL: " + entity.expandedURL)
      Log.d("--------", "text: " + entity.text)
      Log.d("--------", "start: " + entity.start.toString())
      Log.d("--------", "end: " + entity.end.toString())
      Log.d("--------", "-----")
    }

    Log.d("--------", "--- userMention Entities ---")
    for (entity in status.userMentionEntities) {
      Log.d("--------", "name: " + entity.name)
      Log.d("--------", "screenName: " + entity.screenName)
      Log.d("--------", "-----")
    }
  }

}

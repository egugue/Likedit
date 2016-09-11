package com.htoyama.likit.ui.common;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.htoyama.likit.R;

public final class DataBindingAttributeUtil {

  @BindingAdapter("text_homeTagRelatedTweetCount")
  public static void setHomeTagRelatedTweetCount(TextView textView, int tweetCount) {
    String text = textView.getContext()
        .getResources()
        .getString(R.string.home_tag_related_tweet_count, tweetCount);

    textView.setText(text);
  }
}

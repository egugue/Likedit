package com.egugue.licol.ui.common;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.egugue.licol.R;
import com.squareup.picasso.Picasso;

public final class DataBindingAttributeUtil {

  @BindingAdapter("text_homeTagRelatedTweetCount")
  public static void setHomeTagRelatedTweetCount(TextView textView, int tweetCount) {
    String text = textView.getContext()
        .getResources()
        .getString(R.string.home_tag_related_tweet_count, tweetCount);

    textView.setText(text);
  }

  @BindingAdapter("tweetAvatorUrl")
  public static void setTweetAvatorUrl(ImageView imageView, String avatorUrl) {
    Picasso.with(imageView.getContext())
        .load(avatorUrl)
        .into(imageView);
  }
}

package com.htoyama.likit.domain.liked;

import com.htoyama.likit.TweetBuilder;
import com.htoyama.likit.domain.tag.Tag;
import com.htoyama.likit.domain.tag.TagBuilder;
import com.htoyama.likit.domain.tweet.Tweet;

import java.util.Arrays;
import java.util.List;

public class LikedBuilder {
  private Tweet tweet;
  private List<Tag> tagList;

  public LikedTweet build() {
    if (tweet == null) {
      tweet = new TweetBuilder().build();
    }
    if (tagList == null) {
      TagBuilder b = new TagBuilder();
      tagList = Arrays.asList(
          b.setId(1).build(),
          b.setId(2).build());
    }

    return new LikedTweet(tweet, tagList);
  }

  public LikedBuilder setTweet(Tweet tweet) {
    this.tweet = tweet;
    return this;
  }

  public LikedBuilder setTagList(List<Tag> tagList) {
    this.tagList = tagList;
    return this;
  }

}

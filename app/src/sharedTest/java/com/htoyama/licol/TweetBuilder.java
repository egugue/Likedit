package com.htoyama.licol;

import com.htoyama.licol.domain.tweet.Tweet;
import com.htoyama.licol.domain.tweet.Url;
import com.htoyama.licol.domain.tweet.media.Photo;
import com.htoyama.licol.domain.tweet.media.Size;
import com.htoyama.licol.domain.tweet.media.Sizes;
import com.htoyama.licol.domain.tweet.media.Video;
import com.htoyama.licol.domain.user.User;

import java.util.Collections;
import java.util.List;

public class TweetBuilder {
  private static final Sizes SIZES = new Sizes(new Size(1, 2));
  private static final User USER = new UserBuilder().build();
  private static final List<Photo> PHOTO_LIST = Collections.singletonList(
      new PhotoBuilder().build());
  private static final List<Url> URL_LIST = Collections.singletonList(
      new UrlBuilder().build());
  private static final Video VIDEO = new Video("videoUrl", SIZES);

  private long id;
  private User user = USER;
  private long createdAt = System.currentTimeMillis();
  private String text = "text";
  private List<Photo> photoList = PHOTO_LIST;
  private List<Url> urlList = URL_LIST;
  private Video video = VIDEO;

  public Tweet build() {
    return new Tweet(id, user, createdAt, text,
        photoList, urlList, video);
  }

  public TweetBuilder setId(long id) {
    this.id = id;
    return this;
  }

  public TweetBuilder setVideo(Video video) {
    this.video = video;
    return this;
  }

  public TweetBuilder setUrlList(List<Url> urlList) {
    this.urlList = urlList;
    return this;
  }

  public TweetBuilder setPhotoList(List<Photo> photoList) {
    this.photoList = photoList;
    return this;
  }

  public TweetBuilder setText(String text) {
    this.text = text;
    return this;
  }

  public TweetBuilder setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public TweetBuilder setUser(User user) {
    this.user = user;
    return this;
  }

}

package com.htoyama.likit;

import com.htoyama.likit.domain.tweet.media.Size;
import com.htoyama.likit.domain.tweet.media.Sizes;
import com.htoyama.likit.domain.tweet.media.Video;

public class VideoBuilder {
  private String url = "url";
  private Sizes sizes = new Sizes(new Size(1, 2));

  public Video build() {
    return new Video(url, sizes);
  }

  public VideoBuilder setUrl(String url) {
    this.url = url;
    return this;
  }

  public VideoBuilder setSizes(Sizes sizes) {
    this.sizes = sizes;
    return this;
  }
}

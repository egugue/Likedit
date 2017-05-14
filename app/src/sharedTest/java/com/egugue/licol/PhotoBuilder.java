package com.egugue.licol;

import com.egugue.licol.domain.tweet.media.Photo;
import com.egugue.licol.domain.tweet.media.Size;
import com.egugue.licol.domain.tweet.media.Sizes;

public class PhotoBuilder {
  private String url = "url";
  private Sizes sizes = new Sizes(new Size(1, 2));

  public Photo build() {
    return new Photo(url, sizes);
  }

  public PhotoBuilder setUrl(String url) {
    this.url = url;
    return this;
  }

  public PhotoBuilder setSizes(Sizes sizes) {
    this.sizes = sizes;
    return this;
  }

}

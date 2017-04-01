package com.htoyama.likit;

import com.htoyama.likit.domain.tweet.Url;

public class UrlBuilder {
  private String url = "url";
  private String displayUrl = "displayUrl";
  private int start = 1;
  private int end = 2;

  public Url build() {
    return new Url(url, displayUrl, start, end);
  }

  public UrlBuilder setUrl(String url) {
    this.url = url;
    return this;
  }

  public UrlBuilder setDisplayUrl(String displayUrl) {
    this.displayUrl = displayUrl;
    return this;
  }

  public UrlBuilder setStart(int start) {
    this.start = start;
    return this;
  }

  public UrlBuilder setEnd(int end) {
    this.end = end;
    return this;
  }
}

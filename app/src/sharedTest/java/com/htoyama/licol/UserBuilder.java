package com.htoyama.licol;

import com.htoyama.licol.domain.user.User;

public class UserBuilder {
  private long id = 1;
  private String name = "name";
  private String screenName = "screenName";
  private String avatorUrl = "avatorUrl";

  public User build() {
    return new User(id, name, screenName, avatorUrl);
  }

  public UserBuilder setId(long id) {
    this.id = id;
    return this;
  }

  public UserBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public UserBuilder setScreenName(String screenName) {
    this.screenName = screenName;
    return this;
  }

  public UserBuilder setAvatorUrl(String avatorUrl) {
    this.avatorUrl = avatorUrl;
    return this;
  }

}

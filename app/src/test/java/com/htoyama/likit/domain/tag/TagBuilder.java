package com.htoyama.likit.domain.tag;

import java.util.Date;

public class TagBuilder {
  private long id = 1;
  private String name = "name";
  private Date createAt = new Date();

  public Tag build() {
    return new Tag(id, name, createAt);
  }

  public TagBuilder setId(long id) {
    this.id = id;
    return this;
  }

  public TagBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public TagBuilder setCreateAt(Date createAt) {
    this.createAt = createAt;
    return this;
  }

}

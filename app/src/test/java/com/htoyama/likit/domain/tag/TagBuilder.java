package com.htoyama.likit.domain.tag;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TagBuilder {
  private long id = 1;
  private String name = "name";
  private Date createAt = new Date();
  private List<Long> tagIdList = Collections.emptyList();

  public Tag build() {
    return new Tag(id, name, createAt, tagIdList);
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

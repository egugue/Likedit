package com.htoyama.likit.ui.search;

import com.htoyama.likit.domain.tag.Tag;
import com.htoyama.likit.domain.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.htoyama.likit.common.Contract.requireNotNull;

class Assist {
  private static final Assist EMPTY = new Assist(Collections.emptyList());
  private final List<Object> itemList;

  private Assist(List<Object> list) {
    itemList = list;
  }

  int size() {
    return itemList.size();
  }

  Object get(int position) {
    return itemList.get(position);
  }

  static Assist from(List<Tag> tagList, List<User> userList) {
    requireNotNull(tagList);
    requireNotNull(userList);

    List<Object> itemList = new ArrayList<>(tagList.size() + userList.size());
    if (!tagList.isEmpty()) {
      itemList.add(Header.TAG);
      itemList.addAll(tagList);
    }
    if (!userList.isEmpty()) {
      itemList.add(Header.USER);
      itemList.addAll(userList);
    }

    return new Assist(itemList);
  }

  static Assist empty() {
    return EMPTY;
  }

}

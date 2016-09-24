package com.htoyama.likit.ui.search.assist;

import com.htoyama.likit.domain.tag.Tag;
import com.htoyama.likit.domain.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.htoyama.likit.common.Contract.requireNotNull;

public class Assist {
  private static final Assist EMPTY = new Assist(Collections.emptyList());
  private final List<Object> itemList;

  private Assist(List<Object> list) {
    itemList = list;
  }

  public int size() {
    return itemList.size();
  }

  public Object get(int position) {
    return itemList.get(position);
  }

  public static Assist from(List<Tag> tagList, List<User> userList) {
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

  public static Assist empty() {
    return EMPTY;
  }

}

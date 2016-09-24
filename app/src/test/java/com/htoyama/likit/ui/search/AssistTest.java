package com.htoyama.likit.ui.search;

import com.htoyama.likit.UserBuilder;
import com.htoyama.likit.domain.tag.Tag;
import com.htoyama.likit.domain.tag.TagBuilder;
import com.htoyama.likit.domain.user.User;
import com.htoyama.likit.ui.search.assist.Assist;
import com.htoyama.likit.ui.search.assist.Header;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class AssistTest {

  @Test public void from_havingSomeTagsAndUsers() {
    // given
    TagBuilder tb = new TagBuilder();
    Tag tag1 = tb.setId(1).build();
    Tag tag2 = tb.setId(2).build();
    List<Tag> tagList = Arrays.asList(tag1, tag2);

    User user1 = new UserBuilder().setId(1).build();
    List<User> userList = Arrays.asList(user1);

    // when
    Assist assist = Assist.from(tagList, userList);

    // then
    assertThat(assist.size()).isEqualTo(tagList.size() + userList.size() + 2);

    assertThat(assist.get(0)).isEqualTo(Header.TAG);
    assertThat(assist.get(1)).isEqualTo(tag1);
    assertThat(assist.get(2)).isEqualTo(tag2);

    assertThat(assist.get(3)).isEqualTo(Header.USER);
    assertThat(assist.get(4)).isEqualTo(user1);
  }

  @Test public void from_havingSomeTagButNoTag() {
    // given
    TagBuilder tb = new TagBuilder();
    Tag tag1 = tb.setId(1).build();
    Tag tag2 = tb.setId(2).build();
    List<Tag> tagList = Arrays.asList(tag1, tag2);

    List<User> userList = Collections.emptyList();

    // when
    Assist assist = Assist.from(tagList, userList);

    assertThat(assist.size()).isEqualTo(tagList.size() + 1);

    assertThat(assist.get(0)).isEqualTo(Header.TAG);
    assertThat(assist.get(1)).isEqualTo(tag1);
    assertThat(assist.get(2)).isEqualTo(tag2);
  }

  @Test public void from_havingNoTagButSomeUsers() {
    // given
    List<Tag> tagList = Collections.emptyList();

    UserBuilder ub = new UserBuilder();
    User user1 = ub.setId(1).build();
    User user2 = ub.setId(2).build();
    List<User> userList = Arrays.asList(user1, user2);

    // when
    Assist assist = Assist.from(tagList, userList);

    // then
    assertThat(assist.size()).isEqualTo(userList.size() + 1);

    assertThat(assist.get(0)).isEqualTo(Header.USER);
    assertThat(assist.get(1)).isEqualTo(user1);
    assertThat(assist.get(2)).isEqualTo(user2);
  }

  @Test public void from_havingNoItems() {
    List<Tag> tagList = Collections.emptyList();
    List<User> userList = Collections.emptyList();

    Assist assist = Assist.from(tagList, userList);

    assertThat(assist.size()).isEqualTo(0);
  }

  @Test public void empty() {
    Assist assist = Assist.empty();
    assertThat(assist.size()).isEqualTo(0);
  }

}
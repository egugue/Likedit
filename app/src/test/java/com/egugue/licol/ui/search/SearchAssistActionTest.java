package com.egugue.licol.ui.search;

import com.egugue.licol.UserBuilder;
import com.egugue.licol.domain.tag.Tag;
import com.egugue.licol.domain.tag.TagBuilder;
import com.egugue.licol.domain.tag.TagRepository;
import com.egugue.licol.domain.user.User;
import com.egugue.licol.domain.user.UserRepository;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.TestObserver;

import static com.google.common.truth.Truth.assertThat;

@Ignore("until implementing repository using SQLite")
public class SearchAssistActionTest {
  @Mock UserRepository userRepository;
  @Mock TagRepository tagRepository;
  private SearchAssistAction action;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    action = new SearchAssistAction(userRepository, tagRepository);
  }

  @Test public void getAssist_emitAssist() {
    String query = "foo";
    List<Tag> tagList = tagList(2);
    /*
    when(userRepository.findByNameContaining(query))
        .thenReturn(Single.just(Collections.emptyList()));
    when(tagRepository.findByNameContaining(query))
        .thenReturn(Single.just(tagList));
        */

    TestObserver<Assist> test = action.getAssist(query).test();
    test.awaitTerminalEvent();

    test.assertNoErrors();
    test.assertComplete();
    Assist emitted = test.values().get(0);
    assertThat(emitted.size()).isEqualTo(tagList.size() + 1); // 1 means tag header.
  }

  private List<Tag> tagList(int count) {
    List<Tag> tagList = new ArrayList<>(count);
    TagBuilder b = new TagBuilder();

    for (int i = 0; i < count; i++) {
      tagList.add(b.setId(i).build());
    }
    return tagList;
  }

  private List<User> userList(int count) {
    List<User> userList = new ArrayList<>(count);
    UserBuilder b = new UserBuilder();

    for (int i = 0; i < count; i++) {
      userList.add(b.setId(i).build());
    }
    return userList;
  }
}

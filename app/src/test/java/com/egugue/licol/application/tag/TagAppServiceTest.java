package com.egugue.licol.application.tag;

import com.egugue.licol.domain.tag.Tag;
import com.egugue.licol.domain.tag.TagRepository;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@Ignore("until implementing repository using SQLite")
public class TagAppServiceTest {
  @Mock TagRepository repository;
  private TagAppService service;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    service = new TagAppService(repository);
  }

  @Test public void registerNewTag_shouldEmitOneTag() {
    final long expectedId = 3;
    final String expectedName = "foo";

    when(repository.publishNextIdentity())
        .thenReturn(expectedId);
    when(repository.store(any(Tag.class)))
        .thenReturn(Single.just(1L));

    TestObserver<Tag> test = service.registerNewTag(expectedName).test();

    test.awaitTerminalEvent();
    test.assertNoErrors();
    test.assertComplete();

    Tag tag = test.values().get(0);
    assertThat(tag.getId()).isEqualTo(expectedId);
    assertThat(tag.getName()).isEqualTo(expectedName);
  }

  @Test public void findAll() {
    List<Tag> expected = Collections.emptyList();

    TestObserver<List<Tag>> test = service.findAll().test();

    test.awaitTerminalEvent();
    test.assertNoErrors();
    test.assertComplete();
    test.assertValue(expected);
  }

}

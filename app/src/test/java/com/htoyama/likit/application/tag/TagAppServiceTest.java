package com.htoyama.likit.application.tag;

import com.htoyama.likit.domain.tag.Tag;
import com.htoyama.likit.domain.tag.TagRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.when;

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
        .thenReturn(Observable.<Void>just(null));

    TestSubscriber<Tag> sub = new TestSubscriber<>();
    service.registerNewTag(expectedName)
        .subscribe(sub);

    sub.awaitTerminalEvent();
    sub.assertNoErrors();
    sub.assertCompleted();

    Tag tag = sub.getOnNextEvents().get(0);
    assertThat(tag.getId()).isEqualTo(expectedId);
    assertThat(tag.getName()).isEqualTo(expectedName);
  }

  @Test public void findAll() {
    List<Tag> expected = new ArrayList<>();
    when(repository.findAll())
        .thenReturn(Observable.just(expected));

    TestSubscriber<List<Tag>> sub = new TestSubscriber<>();
    service.findAll().subscribe(sub);

    sub.awaitTerminalEvent();
    sub.assertNoErrors();
    sub.assertCompleted();
    List<Tag> actual = sub.getOnNextEvents().get(0);
    assertThat(actual).isEqualTo(expected);
  }

}

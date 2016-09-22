package com.htoyama.likit.application.tag;

import com.htoyama.likit.common.Irrelevant;
import com.htoyama.likit.data.liked.LikedRealmGateway;
import com.htoyama.likit.domain.tag.Tag;
import com.htoyama.likit.domain.tag.TagRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.subscribers.TestSubscriber;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class TagAppServiceTest {
  @Mock TagRepository repository;
  @Mock LikedRealmGateway gateway;
  private TagAppService service;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    service = new TagAppService(repository, gateway);
  }

  @Test public void registerNewTag_shouldEmitOneTag() {
    final long expectedId = 3;
    final String expectedName = "foo";

    when(repository.publishNextIdentity())
        .thenReturn(expectedId);
    when(repository.store(any(Tag.class)))
        .thenReturn(Single.just(Irrelevant.get()));

    TestSubscriber<Tag> sub = service.registerNewTag(expectedName).test();

    sub.awaitTerminalEvent();
    sub.assertNoErrors();
    sub.assertComplete();

    Tag tag = (Tag) sub.getEvents().get(0).get(0);
    assertThat(tag.getId()).isEqualTo(expectedId);
    assertThat(tag.getName()).isEqualTo(expectedName);
  }

  @Test public void findAll() {
    List<Tag> expected = Collections.emptyList();
    when(repository.findAll())
        .thenReturn(Single.just(expected));

    TestSubscriber<List<Tag>> sub = service.findAll().test();

    sub.awaitTerminalEvent();
    sub.assertNoErrors();
    sub.assertComplete();
    sub.assertValue(expected);
  }

}

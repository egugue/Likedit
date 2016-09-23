package com.htoyama.likit.ui.home.tag;

import com.htoyama.likit.application.tag.TagAppService;
import com.htoyama.likit.application.tag.TagTweetCountDto;
import com.htoyama.likit.domain.tag.Tag;
import com.htoyama.likit.domain.tag.TagBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HomeTagPresenterTest {
  @Mock TagAppService service;
  @Mock HomeTagPresenter.View view;
  private HomeTagPresenter presenter;

  @Before public void setUp() {
    RxJavaPlugins.setIoSchedulerHandler(cheduler -> Schedulers.trampoline());
    RxAndroidPlugins.setMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());

    MockitoAnnotations.initMocks(this);
    presenter = new HomeTagPresenter(service);
    presenter.setView(view);
  }

  @After public void tearDown() {
    RxAndroidPlugins.reset();
    RxJavaPlugins.reset();
    presenter.dispose();
  }

  @Ignore("https://github.com/ReactiveX/RxAndroid/issues/332")
  @Test public void loadAllTags_shouldInvokeShowAllTags_whenTagListIsNotEmpty() {
    List<TagTweetCountDto> tagList = Collections.singletonList(new TagTweetCountDto(
        new TagBuilder().build(), 1
    ));
    when(service.findAllWithTweetCount()).thenReturn(Single.just(tagList));
    presenter.loadAllTags();

    verify(view).showProgress();
    verify(view).showAllTags(tagList);
    verify(view, never()).showEmptyState();
  }

  @Ignore("https://github.com/ReactiveX/RxAndroid/issues/332")
  @Test public void loadAllTags_shouldInvokeShowEmptyState_whenTagListIsEmpty() {
    List<TagTweetCountDto> emptyList = new ArrayList<>();
    when(service.findAllWithTweetCount()).thenReturn(Single.just(emptyList));
    presenter.loadAllTags();

    verify(view).showProgress();
    verify(view, never()).showAllTags(emptyList);
    verify(view).showEmptyState();
  }

  @Ignore("https://github.com/ReactiveX/RxAndroid/issues/332")
  @Test public void registerNewTag_shouldInvokeGoToMethod() {
    Tag expected = new TagBuilder().setName("foo").build();
    when(service.registerNewTag(expected.getName()))
        .thenReturn(Single.just(expected));

    presenter.registerNewTag(expected.getName());

    verify(view).goToTagTweetSelectScreen(expected);
  }

}
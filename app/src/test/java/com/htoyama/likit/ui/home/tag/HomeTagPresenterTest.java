package com.htoyama.likit.ui.home.tag;

import com.htoyama.likit.application.tag.TagAppService;
import com.htoyama.likit.domain.tag.Tag;
import com.htoyama.likit.domain.tag.TagBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HomeTagPresenterTest {
  @Mock TagAppService service;
  @Mock HomeTagPresenter.View view;
  private HomeTagPresenter presenter;

  @BeforeClass public static void onlyOnece() {
    RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
      @Override public Scheduler getMainThreadScheduler() {
        return Schedulers.immediate();
      }
    });

    RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
      @Override public Scheduler getIOScheduler() {
        return Schedulers.immediate();
      }
    });
  }

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    presenter = new HomeTagPresenter(service);
    presenter.setView(view);
  }

  @After public void tearDown() {
    presenter.unsubscribe();
  }

  @Test public void loadAllTags_shouldInvokeShowAllTags_whenTagListIsNotEmpty() {
    List<Tag> tagList = Collections.singletonList(new TagBuilder().build());
    when(service.findAll()).thenReturn(Observable.just(tagList));
    presenter.loadAllTags();

    verify(view).showProgress();
    verify(view).showAllTags(tagList);
    verify(view).hideProgress();
    verify(view, never()).showEmptyState();
  }

  @Test public void loadAllTags_shouldInvokeShowEmptyState_whenTagListIsEmpty() {
    List<Tag> emptyList = new ArrayList<>();
    when(service.findAll()).thenReturn(Observable.just(emptyList));
    presenter.loadAllTags();

    verify(view).showProgress();
    verify(view, never()).showAllTags(emptyList);
    verify(view).hideProgress();
    verify(view).showEmptyState();
  }

  @Test public void registerNewTag_shouldInvokeGoToMethod() {
    Tag expected = new TagBuilder().setName("foo").build();
    when(service.registerNewTag(expected.getName()))
        .thenReturn(Observable.just(expected));

    presenter.registerNewTag(expected.getName());

    verify(view).goToTagTweetSelectScreen(expected);
  }

}
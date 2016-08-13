package com.htoyama.likit.ui.common.base;

import org.junit.Before;
import org.junit.Test;

import rx.observers.TestSubscriber;

import static com.google.common.truth.Truth.assertThat;

public class BasePresenterTest {
  private BasePresenter<Object> presenter;

  @Before public void setUp() {
    presenter = new BasePresenter<Object>(){ };
  }

  @Test public void setView() {
    Object expected = new Object();
    presenter.setView(expected);
    assertThat(presenter.view).isEqualTo(expected);
  }

  @Test public void unsubscribe() {
    presenter.setView(new Object());
    presenter.subs.add(new TestSubscriber<Void>());

    presenter.unsubscribe();

    assertThat(presenter.view).isNull();
    assertThat(presenter.subs.hasSubscriptions()).isFalse();
    assertThat(presenter.subs.isUnsubscribed()).isFalse();
  }

  @Test public void isUnsubscribed() {
    assertThat(presenter.isUnsubscribed())
        .isEqualTo(presenter.subs.isUnsubscribed());
  }

}
package com.htoyama.likit.ui.common.base;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subscribers.TestSubscriber;

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
    presenter.disposables.add(new TestSubscriber<>());

    presenter.dispose();

    assertThat(presenter.view).isNull();
    CompositeDisposable dis = presenter.disposables;
    assertThat(dis.size()).isEqualTo(0);
    assertThat(dis.isDisposed()).isFalse();
  }

  @Test public void isUnsubscribed() {
    assertThat(presenter.isDisposed())
        .isEqualTo(presenter.disposables.isDisposed());
  }

}
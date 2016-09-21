package com.htoyama.likit.ui.search;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.htoyama.likit.R;
import com.htoyama.likit.databinding.ActivitySearchBinding;
import com.htoyama.likit.ui.common.DividerItemDecoration;
import com.htoyama.likit.ui.common.activity.BaseRxActivity;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class SearchActivity extends BaseRxActivity implements Presenter.View {

  private ActivitySearchBinding binding;
  @Inject Presenter presenter;
  @Inject AssistAdapter adapter;

  public static Intent createIntent(Context context) {
    return new Intent(context, SearchActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

    SearchComponent.Initializer
        .init(this)
        .inject(this);

    initToolbar();
    initList();
    presenter.setView(this);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    presenter.unsubscribe();
  }

  @Override public void showAssist(@NonNull Assist assist) {
    Log.d("ーーー", assist.toString());
    adapter.setAssist(assist);
  }

  private void initList() {
    RecyclerView listView = binding.searchAssistList;
    listView.setAdapter(adapter);
    listView.addItemDecoration(new DividerItemDecoration(this));
    listView.setLayoutManager(new LinearLayoutManager(this));

    RxTextView.afterTextChangeEvents(binding.searchQuery)
        .compose(bindToLifecycle())
        .throttleLast(500, TimeUnit.MILLISECONDS)
        .map(event -> event.editable().toString())
        .distinctUntilChanged()
        .filter(query -> query.length() > 3)
        .subscribe(query -> {
          presenter.loadAssist(query);
        });
  }

  private void initToolbar() {
    setSupportActionBar(binding.toolbar);

    ActionBar bar = getSupportActionBar();
    if (bar != null) {
      bar.setDisplayHomeAsUpEnabled(true);
      bar.setDisplayShowHomeEnabled(true);
      bar.setDisplayShowTitleEnabled(false);
      bar.setHomeButtonEnabled(true);
    }
  }

}

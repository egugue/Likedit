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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.htoyama.likit.R;
import com.htoyama.likit.databinding.ActivitySearchBinding;
import com.htoyama.likit.domain.tag.Tag;
import com.htoyama.likit.domain.user.User;
import com.htoyama.likit.ui.common.activity.BaseRxActivity;
import com.htoyama.likit.ui.search.assist.Assist;
import com.htoyama.likit.ui.search.assist.AssistAdapter;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

import static com.htoyama.likit.common.Contract.requireNotNull;

public class SearchActivity extends BaseRxActivity
    implements Presenter.View, AssistAdapter.OnItemClickListener {

  @Inject Presenter presenter;

  private ActivitySearchBinding binding;
  private AssistAdapter adapter;

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
    initSearchEditText();
    presenter.setView(this);
  }

  @Override protected void onDestroy() {
    presenter.dispose();
    super.onDestroy();
  }

  @Override public void showAssist(@NonNull Assist assist) {
    adapter.setAssist(assist);
  }

  @Override public void onTagClick(Tag tag) {
    Log.d("ーーー", "OnTagClick. Tag = " + tag.toString());
  }

  @Override public void onUserClick(User user) {
    Log.d("ーーー", "OnUserClick. User = " + user.toString());
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

  private void initList() {
    RecyclerView listView = binding.searchAssistList;
    adapter = new AssistAdapter(this);
    listView.setAdapter(adapter);
    listView.setLayoutManager(new LinearLayoutManager(this));
  }

  private void initSearchEditText() {
    requireNotNull(adapter);
    requireNotNull(presenter);

    EditText searchView = binding.searchQuery;

    // When user input search query.
    RxTextView.afterTextChangeEvents(searchView)
        .compose(bindToLifecycle())
        .throttleLast(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
        .map(event -> event.editable().toString())
        .distinctUntilChanged()
        .subscribe(query -> {
          int length = query.length();

          if (length == 0) {
            adapter.setAssist(Assist.empty());
          } else if (length >= 3) {
            presenter.loadAssist(query);
          }
        });

    // When user input search button.
    RxTextView.editorActionEvents(searchView)
        .filter(event -> event.actionId() == EditorInfo.IME_ACTION_SEARCH)
        .compose(bindToLifecycle())
        .subscribe(searchEvent -> {
          TextView v = searchEvent.view();

          InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
          imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

          String query = v.getText().toString();
          Log.d("ーーー", query);
        });
  }
}
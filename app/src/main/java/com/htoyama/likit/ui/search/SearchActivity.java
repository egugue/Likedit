package com.htoyama.likit.ui.search;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
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
import com.htoyama.likit.ui.search.assist.SearchAssistFragment;
import com.jakewharton.rxbinding.widget.RxTextView;

public class SearchActivity extends BaseRxActivity
    implements SearchAssistFragment.Listener {

  public ActivitySearchBinding binding;
  public SearchComponent component;

  public static Intent createIntent(Context context) {
    return new Intent(context, SearchActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

    component = SearchComponent.Initializer.init(this);

    initToolbar();
    initSearchEditText();
    replaceFragment(SearchAssistFragment.newInstance());
  }

  @Override public void onTagClick(Tag tag) {
    Log.d("ーーー", "OnTagClick. Tag = " + tag.toString());
  }

  @Override public void onUserClick(User user) {
    Log.d("ーーー", "OnUserClick. User = " + user.toString());
  }

  private void replaceFragment(Fragment fragment) {
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.content, fragment, fragment.getClass().getSimpleName());
    ft.commit();
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

  private void initSearchEditText() {
    EditText searchView = binding.searchQuery;

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
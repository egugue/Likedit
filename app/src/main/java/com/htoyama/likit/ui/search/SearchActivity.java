package com.htoyama.likit.ui.search;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.htoyama.likit.R;
import com.htoyama.likit.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

  private ActivitySearchBinding binding;

  public static Intent createIntent(Context context) {
    return new Intent(context, SearchActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

    initToolbar();
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

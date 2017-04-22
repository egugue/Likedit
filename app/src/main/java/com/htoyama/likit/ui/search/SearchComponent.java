package com.htoyama.likit.ui.search;

import android.content.Context;

import com.htoyama.likit.App;
import com.htoyama.likit.old.AppComponent;

import dagger.Component;

@SearchScope
@Component(dependencies = AppComponent.class)
public interface SearchComponent {
  void inject(SearchActivity activity);

  class Initializer {
    static SearchComponent init(Context context) {
      return DaggerSearchComponent.builder()
          .appComponent(App.oldComponent(context))
          .build();
    }
  }
}

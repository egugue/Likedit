package com.htoyama.licol.ui.search;

import android.content.Context;

import com.htoyama.licol.App;
import com.htoyama.licol.AppComponent;

import dagger.Component;

@SearchScope
@Component(dependencies = AppComponent.class)
public interface SearchComponent {
  void inject(SearchActivity activity);

  class Initializer {
    static SearchComponent init(Context context) {
      return DaggerSearchComponent.builder()
          .appComponent(App.component(context))
          .build();
    }
  }
}

package com.htoyama.likit.ui.search;

import android.content.Context;

import com.htoyama.likit.App;
import com.htoyama.likit.AppComponent;
import com.htoyama.likit.ui.search.assist.SearchAssistFragment;
import com.htoyama.likit.ui.search.result.SearchResultFragment;

import dagger.Component;

@SearchScope
@Component(dependencies = AppComponent.class)
public interface SearchComponent {
  void inject(SearchActivity activity);
  void inject(SearchAssistFragment fragment);
  void inject(SearchResultFragment fragment);

  class Initializer {
    static SearchComponent init(Context context) {
      return DaggerSearchComponent.builder()
          .appComponent(App.component(context))
          .build();
    }
  }
}

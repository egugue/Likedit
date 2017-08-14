package com.egugue.licol.ui.search;

import android.content.Context;

import com.egugue.licol.App;
import com.egugue.licol.AppComponent;
import com.egugue.licol.ui.ActivityScope;

import com.egugue.licol.ui.search.result.SearchResultFragment;
import dagger.Component;
import org.jetbrains.annotations.NotNull;

@ActivityScope
@Component(dependencies = AppComponent.class)
public interface SearchComponent {
  void inject(SearchActivity activity);

  void inject(@NotNull SearchResultFragment searchResultFragment);

  class Initializer {
    static SearchComponent init(Context context) {
      return DaggerSearchComponent.builder()
          .appComponent(App.component(context))
          .build();
    }
  }
}

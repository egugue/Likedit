package com.egugue.licol.ui.search;

import android.content.Context;

import com.egugue.licol.App;
import com.egugue.licol.AppComponent;
import com.egugue.licol.ui.ActivityScope;

import dagger.Component;

@ActivityScope
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

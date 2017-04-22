package com.htoyama.likit;

import com.htoyama.likit.data.DataModule;
import com.htoyama.likit.domain.liked.LikedRepository;
import com.htoyama.likit.domain.tag.TagRepository;
import com.htoyama.likit.domain.user.UserRepository;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by htoyama on 2017/04/23.
 */

@Singleton
@Component(modules ={
    AndroidSupportInjectionModule.class,
    AppModule.class,
    BuildersModule.class })
public interface AppComponent {

  @Component.Builder interface Builder {
    @BindsInstance
    Builder application(App application);
    AppComponent build();
  }

  void inject(App app);

  /*
  LikedRepository likedRepository();
  TagRepository tagRepository();
  UserRepository userRepository();
  */
}

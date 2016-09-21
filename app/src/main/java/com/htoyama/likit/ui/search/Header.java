package com.htoyama.likit.ui.search;

import android.content.Context;

public enum Header {
  TAG {
    @Override String displayingText(Context context) {
      return "タグ";
    }
  },
  USER {
    @Override String displayingText(Context context) {
      return "ユーザー";
    }
  };

  abstract String displayingText(Context context);
}

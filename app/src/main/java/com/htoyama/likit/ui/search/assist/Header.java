package com.htoyama.likit.ui.search.assist;

import android.content.Context;

import com.htoyama.likit.R;

public enum Header {

  TAG {
    @Override public String displayingText(Context context) {
      return context.getString(R.string.search_assist_header_tag);
    }
  },
  USER {
    @Override public String displayingText(Context context) {
      return context.getString(R.string.search_assist_header_user);
    }
  };

  public abstract String displayingText(Context context);
}

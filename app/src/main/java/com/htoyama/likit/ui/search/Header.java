package com.htoyama.likit.ui.search;

import android.content.Context;

import com.htoyama.likit.R;

public enum Header {

  TAG {
    @Override String displayingText(Context context) {
      return context.getString(R.string.search_assist_header_tag);
    }
  },
  USER {
    @Override String displayingText(Context context) {
      return context.getString(R.string.search_assist_header_tag);
    }
  };

  abstract String displayingText(Context context);
}

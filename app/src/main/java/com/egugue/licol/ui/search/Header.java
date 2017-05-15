package com.egugue.licol.ui.search;

import android.content.Context;

import com.egugue.licol.R;

public enum Header {

  TAG {
    @Override String displayingText(Context context) {
      return context.getString(R.string.search_assist_header_tag);
    }
  },
  USER {
    @Override String displayingText(Context context) {
      return context.getString(R.string.search_assist_header_user);
    }
  };

  abstract String displayingText(Context context);
}

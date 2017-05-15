package com.egugue.licol.ui.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.egugue.licol.R;

public final class DividerItemDecoration extends RecyclerView.ItemDecoration {
  private final Paint paint;
  private final int dividerHeightPx;

  public DividerItemDecoration(Context context) {
    Resources res = context.getResources();
    paint = new Paint();
    paint.setColor(ResourcesCompat.getColor(res, R.color.divider, context.getTheme()));
    dividerHeightPx = res.getDimensionPixelSize(R.dimen.divider_height);
  }

  @Override public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    int left = parent.getPaddingLeft();
    int right = parent.getWidth() - parent.getPaddingRight();
    int childCount = parent.getChildCount();

    for (int i = 0; i < childCount; i++) {
      View child = parent.getChildAt(i);
      float top = child.getBottom();
      float bottom = top + dividerHeightPx;
      c.drawRect(left, top, right, bottom, paint);
    }
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    outRect.bottom = dividerHeightPx;
  }

}
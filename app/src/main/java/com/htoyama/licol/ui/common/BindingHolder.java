package com.htoyama.licol.ui.common;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * A simple {@link RecyclerView.ViewHolder} having a {@link ViewDataBinding}.
 */
public abstract class BindingHolder<T extends ViewDataBinding>
    extends RecyclerView.ViewHolder {

  public final T binding;

  public BindingHolder(ViewGroup parent, @LayoutRes int layoutRes) {
    super(LayoutInflater.from(parent.getContext())
        .inflate(layoutRes, parent, false));

    binding = DataBindingUtil.bind(itemView);
  }

}

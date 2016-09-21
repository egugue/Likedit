package com.htoyama.likit.ui.search;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates2.AdapterDelegate;
import com.htoyama.likit.R;
import com.htoyama.likit.databinding.ViewSearchAssistTagBinding;
import com.htoyama.likit.domain.tag.Tag;
import com.htoyama.likit.ui.common.BindingHolder;

public class TagAdapterDelegate implements AdapterDelegate<Assist> {

  @Override public boolean isForViewType(@NonNull Assist items, int position) {
    return items.get(position) instanceof Tag;
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
    return new ViewHolder(parent, R.layout.view_search_assist_tag);
  }

  @Override
  public void onBindViewHolder(@NonNull Assist items, int position, @NonNull RecyclerView.ViewHolder holder) {
    ((ViewHolder) holder).binding.setTag(
        (Tag) items.get(position));
  }

  private static class ViewHolder extends BindingHolder<ViewSearchAssistTagBinding> {

    public ViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
      super(parent, layoutRes);
    }
  }
}

package com.htoyama.likit.ui.search.assist;

import android.databinding.BindingAdapter;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates2.AdapterDelegate;
import com.htoyama.likit.R;
import com.htoyama.likit.databinding.ViewSearchAssistHeaderBinding;
import com.htoyama.likit.ui.common.BindingHolder;

public class HeaderAdapterDelegate implements AdapterDelegate<Assist> {

  @Override public boolean isForViewType(@NonNull Assist items, int position) {
    return items.get(position) instanceof Header;
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
    return new ViewHolder(parent, R.layout.view_search_assist_header);
  }

  @Override
  public void onBindViewHolder(@NonNull Assist items, int position,
                               @NonNull RecyclerView.ViewHolder holder) {
    ((ViewHolder) holder).binding.setHeader(
        (Header) items.get(position));
  }

  private static class ViewHolder extends BindingHolder<ViewSearchAssistHeaderBinding> {

    ViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
      super(parent, layoutRes);
    }

  }

  @BindingAdapter("searchAssistHeader")
  public static void setSearchAssistHeader(TextView textView, Header header) {
    textView.setText(
        header.displayingText(
            textView.getContext()));
  }

}
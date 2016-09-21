package com.htoyama.likit.ui.search;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates2.AdapterDelegate;
import com.htoyama.likit.R;
import com.htoyama.likit.databinding.ViewSearchAssistUserBinding;
import com.htoyama.likit.domain.user.User;
import com.htoyama.likit.ui.common.BindingHolder;

public class UserAdapterDelegate implements AdapterDelegate<Assist> {
  private static final String TAG = UserAdapterDelegate.class.getSimpleName();

  @Override public boolean isForViewType(@NonNull Assist items, int position) {
    return items.get(position) instanceof User;
  }

  @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
    return new ViewHolder(parent, R.layout.view_search_assist_user);
  }

  @Override
  public void onBindViewHolder(@NonNull Assist items, int position, @NonNull RecyclerView.ViewHolder holder) {
    ((ViewHolder) holder).binding.setUser(
        (User) items.get(position)
    );
  }

  private static class ViewHolder extends BindingHolder<ViewSearchAssistUserBinding> {

    public ViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
      super(parent, layoutRes);
    }
  }
}

package com.htoyama.likit.ui.search;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates2.AdapterDelegatesManager;

import javax.inject.Inject;

@SearchScope
class AssistAdapter extends RecyclerView.Adapter {

  private final AdapterDelegatesManager<Assist> manager;
  private Assist assist = Assist.empty();

  @Inject public AssistAdapter() {
    manager = new AdapterDelegatesManager<>();
    manager.addDelegate(new HeaderAdapterDelegate());
    manager.addDelegate(new TagAdapterDelegate());
    manager.addDelegate(new UserAdapterDelegate());
  }

  public void setAssist(Assist assist) {
    this.assist = assist;
    notifyDataSetChanged();
  }

  @Override public int getItemViewType(int position) {
    return manager.getItemViewType(assist, position);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return manager.onCreateViewHolder(parent, viewType);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    manager.onBindViewHolder(assist, position, holder);
  }

  @Override public int getItemCount() {
    return assist.size();
  }

}

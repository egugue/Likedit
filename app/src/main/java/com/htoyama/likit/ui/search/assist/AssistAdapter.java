package com.htoyama.likit.ui.search.assist;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates2.AdapterDelegatesManager;
import com.htoyama.likit.domain.tag.Tag;
import com.htoyama.likit.domain.user.User;

public class AssistAdapter extends RecyclerView.Adapter {

  public interface OnItemClickListener {
    void onTagClick(Tag tag);
    void onUserClick(User user);
  }

  private final AdapterDelegatesManager<Assist> manager;
  private Assist assist = Assist.empty();

  public AssistAdapter(OnItemClickListener listener) {
    manager = new AdapterDelegatesManager<>();
    manager.addDelegate(new HeaderAdapterDelegate());
    manager.addDelegate(new TagAdapterDelegate(listener::onTagClick));
    manager.addDelegate(new UserAdapterDelegate(listener::onUserClick));
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

package com.htoyama.likit.ui.search.assist;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.htoyama.likit.R;
import com.htoyama.likit.databinding.FragmentSearchAssistBinding;
import com.htoyama.likit.domain.tag.Tag;
import com.htoyama.likit.domain.user.User;
import com.htoyama.likit.ui.common.base.BaseRxFragment;
import com.htoyama.likit.ui.search.SearchActivity;
import com.jakewharton.rxbinding.widget.RxTextView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

import static com.htoyama.likit.common.Contract.requireNotNull;

public class SearchAssistFragment extends BaseRxFragment
    implements Presenter.View, AssistAdapter.OnItemClickListener {

  @Inject Presenter presenter;

  @Nullable private Listener mListener;
  private FragmentSearchAssistBinding binding;
  private AssistAdapter adapter;

  public interface Listener {
    void onTagClick(Tag tag);
    void onUserClick(User user);
  }

  public static SearchAssistFragment newInstance() {
    return new SearchAssistFragment();
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);

    ((SearchActivity) context).component
        .inject(this);
    mListener = (Listener) context;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
        R.layout.fragment_search_assist, container, false);
    return binding.getRoot();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initList();
  }

  @Override public void onStart() {
    super.onStart();
    presenter.setView(this);
    initSearchQueryStream();
  }

  @Override public void onStop() {
    presenter.dispose();
    super.onStop();
  }

  @Override public void onDetach() {
    mListener = null;
    super.onDetach();
  }

  @Override public void showAssist(@NotNull Assist assist) {
    adapter.setAssist(assist);
  }

  @Override public void onTagClick(Tag tag) {
    if (mListener != null) {
      mListener.onTagClick(tag);
    }
  }

  @Override public void onUserClick(User user) {
    if (mListener != null) {
      mListener.onUserClick(user);
    }
  }

  private void initList() {
    RecyclerView listView = binding.searchAssistList;
    adapter = new AssistAdapter(this);
    listView.setAdapter(adapter);
    listView.setLayoutManager(new LinearLayoutManager(getContext()));
  }

  private void initSearchQueryStream() {
    requireNotNull(presenter);

    // When user input search query.
    EditText queryView = ((SearchActivity) getContext()).binding.searchQuery;

    RxTextView.afterTextChangeEvents(queryView)
        .compose(bindToLifecycle())
        .throttleLast(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
        .map(event -> event.editable().toString())
        .distinctUntilChanged()
        .subscribe(query -> {
          int length = query.length();

          if (length == 0) {
            adapter.setAssist(Assist.empty());
          } else if (length >= 3) {
            presenter.loadAssist(query);
          }
        });
  }

}
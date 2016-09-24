package com.htoyama.likit.ui.search.result;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.htoyama.likit.R;
import com.htoyama.likit.databinding.FragmentSearchResultBinding;
import com.htoyama.likit.ui.common.base.BaseRxFragment;
import com.htoyama.likit.ui.search.SearchActivity;

import javax.inject.Inject;

import static com.htoyama.likit.common.Contract.requireNotNull;

public class SearchResultFragment extends BaseRxFragment implements Presenter.View {

  public interface Listener {

  }

  private static final String ARG_QUERY = "query";

  @Inject Presenter presenter;

  @Nullable private Listener listener;
  private String query;
  private FragmentSearchResultBinding binding;

  public static SearchResultFragment newInstance(String query) {
    SearchResultFragment fragment = new SearchResultFragment();
    Bundle b = new Bundle();
    b.putString(ARG_QUERY, query);
    fragment.setArguments(b);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle bundle = requireNotNull(getArguments());
    query = requireNotNull(bundle.getString(ARG_QUERY));

    Log.d("ーーー", query);
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);

    ((SearchActivity) context).component
        .inject(this);
    listener = (Listener) context;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
        R.layout.fragment_search_result, container, false);
    return binding.getRoot();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override public void onStart() {
    super.onStart();
    presenter.setView(this);
  }

  @Override public void onStop() {
    presenter.dispose();
    super.onStop();
  }

  @Override public void onDetach() {
    listener = null;
    super.onDetach();
  }
}

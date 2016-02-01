package com.yahoo.reportr.ui.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.Blog;
import com.yahoo.reportr.data.entity.Post;
import com.yahoo.reportr.data.entity.Topic;
import com.yahoo.reportr.ui.DaggerReportrComponent;
import com.yahoo.reportr.ui.presenter.BlogsPresenter;
import com.yahoo.reportr.ui.view.adapter.BlogsRecyclerAdapter;
import com.yahoo.reportr.ui.view.views.BlogsView;
import com.yahoo.reportr.utils.Constants;
import com.yahoo.reportr.utils.DividerItemDecoration;
import com.yahoo.reportr.utils.MemCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavanis on 1/14/16.
 */
public class BlogFragment extends Fragment {

    BlogsRecyclerAdapter adapter;
    //BlogsPresenter blogsPresenter;
    RecyclerView rvBlogs;
    private String authkey;
    private Topic topic;

    public static BlogFragment newInstance(Bundle args) {
        BlogFragment fragment = new BlogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        authkey = args.getString(Constants.KEY_AUTHKEY);
        topic = args.getParcelable(Constants.KEY_TOPIC);
        adapter = new BlogsRecyclerAdapter(authkey, topic);
        //DaggerReportrComponent.builder().build().inject(blogsPresenter);

        List<Blog> blogList = (List<Blog>) MemCache.getInstance().getLru().get(Constants.BLOG_LIST);
        if (blogList != null) {
            adapter.addAll(blogList);
            adapter.notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bloglist, container, false);
        initializeView(view);
        //setupListener();
        return view;
    }

    private void initializeView(View view) {
        rvBlogs = (RecyclerView) view.findViewById(R.id.rvBlogs);
        rvBlogs.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rvBlogs.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        //Set LayoutManager to RecyclerView
        rvBlogs.setLayoutManager(layoutManager);
        rvBlogs.setAdapter(adapter);
    }
}

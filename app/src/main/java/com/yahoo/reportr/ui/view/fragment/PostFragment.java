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
import com.yahoo.reportr.ui.view.adapter.PostsRecyclerAdapter;
import com.yahoo.reportr.ui.view.views.BlogsView;
import com.yahoo.reportr.utils.Constants;
import com.yahoo.reportr.utils.DividerItemDecoration;
import com.yahoo.reportr.utils.MemCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavanis on 1/15/16.
 */
public class PostFragment extends Fragment {

    //private BlogsPresenter blogsPresenter;
    private String authkey;
    private Topic topic;
    PostsRecyclerAdapter adapter;
    private RecyclerView rvPosts;
    private String blogName;

    public static PostFragment newInstance(Bundle args) {
        PostFragment fragment = new PostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        authkey = args.getString(Constants.KEY_AUTHKEY);
        topic = args.getParcelable(Constants.KEY_TOPIC);
        blogName = args.getString(Constants.KEY_BLOG_NAME);
        adapter = new PostsRecyclerAdapter(authkey, topic, blogName);
        //DaggerReportrComponent.builder().build().inject(blogsPresenter);

        List<Post> posts = (List<Post>) MemCache.getInstance().getLru().get(blogName);
        if (posts != null) {
            adapter.addAll(posts);
            adapter.notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_postlist, container, false);
        initializeView(view);
        return view;
    }

    private void initializeView(View view) {
        rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);
        rvPosts.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rvPosts.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        //Set LayoutManager to RecyclerView
        rvPosts.setLayoutManager(layoutManager);
        rvPosts.setAdapter(adapter);
    }
}

package com.yahoo.reportr.ui.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.AuthInfo;
import com.yahoo.reportr.data.entity.Topic;
import com.yahoo.reportr.data.entity.User;
import com.yahoo.reportr.ui.DaggerReportrComponent;
import com.yahoo.reportr.ui.presenter.ReportrPresenter;
import com.yahoo.reportr.ui.presenter.UserPresenter;
//import com.yahoo.reportr.ui.view.adapter.OntologyAdapter;
import com.yahoo.reportr.ui.view.activity.UserActivity;
import com.yahoo.reportr.ui.view.adapter.TopicsRecyclerAdapter;
import com.yahoo.reportr.ui.view.views.UserView;
import com.yahoo.reportr.utils.Constants;
import com.yahoo.reportr.utils.DividerItemDecoration;
import com.yahoo.reportr.utils.MemCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavanis on 1/7/16.
 */
public class UserFragment extends Fragment {

    TopicsRecyclerAdapter adapter;
    UserPresenter userPresenter;
    List<Topic> topics;
    RecyclerView rvTopics;
    private String authkey;
    SharedPreferences preferences;
    private SwipeRefreshLayout swipeContainer;
    private UserAsyncTask asyncTask;
    private User userObj;

    public UserFragment() {
    }

    public static UserFragment newInstance(Bundle args) {
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPresenter = new UserPresenter();
        preferences = getActivity().getSharedPreferences(Constants.SHARED_PREF, 0);
        Bundle args = getArguments();
        authkey = args.getString(Constants.KEY_AUTHKEY);
        adapter = new TopicsRecyclerAdapter(getActivity(), authkey);
        userObj = args.getParcelable(Constants.KEY_USER);
        DaggerReportrComponent.builder().build().inject(userPresenter);
        topics = new ArrayList<>();
        topics.addAll(userObj.getTopics());
        adapter.addAll(topics);
        adapter.notifyDataSetChanged();
        //fetchUserInfo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topiclist, container, false);
        initializeView(view);
        setupListener();
        return view;
    }

    private void setupListener() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchUserInfo();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initializeView(View view) {
        rvTopics = (RecyclerView) view.findViewById(R.id.rvTopics);
        rvTopics.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rvTopics.setHasFixedSize(true);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerTopics);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        //Set LayoutManager to RecyclerView
        rvTopics.setLayoutManager(layoutManager);
        rvTopics.setAdapter(adapter);
    }

    public void fetchUserInfo() {
        asyncTask = new UserAsyncTask(getActivity());
        asyncTask.execute(authkey);
    }

    public class UserAsyncTask extends AsyncTask<String, Void, String> {

        User user;
        ProgressDialog dialog;

        public UserAsyncTask(Context context) {
            dialog = new ProgressDialog(context);
        }

        @Override
        protected String doInBackground(String... params) {
            user = userPresenter.getUserInfo(params[0]);
            MemCache.getInstance().getLru().put(Constants.USER_OBJECT, user);
            return params[0];
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeContainer.setRefreshing(false);
            dialog.setMessage("Updating interests..");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            List<Topic> topics = user.getTopics();
            SharedPreferences.Editor editor = preferences.edit();
            if (!user.getOntologies().isEmpty()) {
                editor.putString(Constants.USER_ONTO, user.getOntologies().toString());
                editor.commit();
                adapter.addAll(topics);
                adapter.notifyDataSetChanged();
            } else {
                Intent i = new Intent(getActivity(), UserActivity.class);
                startActivity(i);
            }
        }
    }
}

package com.yahoo.reportr.ui.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.Topic;
import com.yahoo.reportr.data.entity.User;
import com.yahoo.reportr.data.service.BlogListService;
import com.yahoo.reportr.ui.DaggerReportrComponent;
import com.yahoo.reportr.ui.presenter.UserPresenter;
import com.yahoo.reportr.ui.view.fragment.AuthorizeFragment;
import com.yahoo.reportr.ui.view.fragment.OntologyFragment;
import com.yahoo.reportr.ui.view.fragment.UserFragment;
import com.yahoo.reportr.ui.view.views.UserView;
import com.yahoo.reportr.utils.Constants;
import com.yahoo.reportr.utils.MemCache;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String authkey;
    private long tstamp;
    Bundle args;
    UserFragment userFragment;
    SharedPreferences preferences;
    private List<Topic> topics;
    private List<Integer> ontoIds;
    private UserPresenter presenter;
    private UserAsyncTask asyncTask;
    //private ProgressBar progressBar;
    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences(Constants.SHARED_PREF, 0);
        presenter = new UserPresenter();
        DaggerReportrComponent.create().inject(presenter);
        this.topics = new ArrayList<>();
        this.ontoIds = new ArrayList<>();

        setTitle(R.string.requests);
        toolbar.showOverflowMenu();
        authkey = preferences.getString(Constants.KEY_AUTHKEY, "");
        tstamp = preferences.getLong(Constants.KEY_TSTAMP, 0);
        args = new Bundle();
        args.putString(Constants.KEY_AUTHKEY, authkey);
        args.putLong(Constants.KEY_TSTAMP, tstamp);

        User user = (User) MemCache.getInstance().getLru().get(Constants.USER_OBJECT);
        args.putParcelable(Constants.KEY_USER, user);
        handleUser(user);
    }

    private void handleUser(User user) {
        System.out.println("User ontologies: "+user.getOntologies());
        if (!user.getOntologies().isEmpty()) {
            ontoIds.addAll(user.getOntologies());
        }
        if (!user.getTopics().isEmpty()) {
            topics.addAll(user.getTopics());
        }

        //callBlogService();
        if (ontoIds.isEmpty()) {
            preferences.edit().putString(Constants.USER_ONTO, null).commit();
            inflateDynamicLayout("Welcome to Reportr!! Please update your interests");
        } else {
            preferences.edit().putString(Constants.USER_ONTO, ontoIds.toString()).commit();
            getUserInfo();
        }
    }

    private void inflateDynamicLayout(String text) {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dynamic_home_layout, null);
        FrameLayout container = (FrameLayout) findViewById(R.id.flContent);
        container.addView(view);
        TextView tv = (TextView) findViewById(R.id.tvHome);
        tv.setText(text);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerUser);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pollUser();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        Button btnUpdateInterests = (Button) findViewById(R.id.btnUpdate);
        btnUpdateInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInterests();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            case R.id.action_interests:
                updateInterests();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void updateInterests() {
        Intent i = new Intent(this, OntologyActivity.class);
        i.putExtra(Constants.KEY_AUTHKEY, authkey);
        i.putExtra(Constants.KEY_TSTAMP, tstamp);
        startActivity(i);
    }

    public void getUserInfo() {
        if (topics.isEmpty()) {
            inflateDynamicLayout("Hmm, no topics available please update your interests");
        } else {
            userFragment = UserFragment.newInstance(args);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.flContent, userFragment).commit();
        }
    }

    private void pollUser() {
        asyncTask = new UserAsyncTask(this);
        asyncTask.execute(authkey);
    }

    public class UserAsyncTask extends AsyncTask<String, Void, String> {

        private UserActivity userActivity;
        private User user;
        ProgressDialog progressDialog;

        public UserAsyncTask(UserActivity activity) {
            userActivity = activity;
            progressDialog = new ProgressDialog(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            user = presenter.getUserInfo(params[0]);
            MemCache.getInstance().getLru().put(Constants.USER_OBJECT, user);
            return params[0];
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Updating interests..");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            userActivity.finish();
            Intent i = new Intent(userActivity, UserActivity.class);
            startActivity(i);
        }
    }

}

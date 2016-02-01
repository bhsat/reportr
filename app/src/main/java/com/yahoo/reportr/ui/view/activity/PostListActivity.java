package com.yahoo.reportr.ui.view.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.Blog;
import com.yahoo.reportr.data.entity.Post;
import com.yahoo.reportr.data.entity.Topic;
import com.yahoo.reportr.ui.view.fragment.BlogFragment;
import com.yahoo.reportr.ui.view.fragment.PostFragment;
import com.yahoo.reportr.utils.Constants;

public class PostListActivity extends AppCompatActivity {

    private Blog blog;
    private Topic topic;
    private String authkey;
    private PostFragment postFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        blog = getIntent().getParcelableExtra(Constants.KEY_BLOG);
        topic = getIntent().getParcelableExtra(Constants.KEY_TOPIC);
        authkey = getIntent().getStringExtra(Constants.KEY_AUTHKEY);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Select Post");
        showPosts();
    }

    public void showPosts() {
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_TOPIC, topic);
        args.putString(Constants.KEY_AUTHKEY, authkey);
        args.putString(Constants.KEY_BLOG_NAME, blog.getName());
        postFragment = PostFragment.newInstance(args);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.flPosts, postFragment).commit();
    }
}

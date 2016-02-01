package com.yahoo.reportr.ui.view.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.Post;
import com.yahoo.reportr.data.entity.Topic;
import com.yahoo.reportr.ui.view.fragment.PostFragment;
import com.yahoo.reportr.ui.view.fragment.SubmissionFragment;
import com.yahoo.reportr.utils.Constants;

public class SubmissionActivity extends AppCompatActivity {

    private Topic topic;
    private String authkey;
    private String blogName;
    private Post post;
    private SubmissionFragment submissionFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        topic = getIntent().getParcelableExtra(Constants.KEY_TOPIC);
        authkey = getIntent().getStringExtra(Constants.KEY_AUTHKEY);
        blogName = getIntent().getStringExtra(Constants.KEY_BLOG_NAME);
        post = getIntent().getParcelableExtra(Constants.KEY_POST);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Preview");
        showPPostPreview();
    }

    public void showPPostPreview() {
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_TOPIC, topic);
        args.putString(Constants.KEY_AUTHKEY, authkey);
        args.putString(Constants.KEY_BLOG_NAME, blogName);
        args.putParcelable(Constants.KEY_POST, post);
        submissionFragment = SubmissionFragment.newInstance(args);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.flPreview, submissionFragment).commit();
    }
}

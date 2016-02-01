package com.yahoo.reportr.ui.view.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.Topic;
import com.yahoo.reportr.ui.view.fragment.TopicFragment;
import com.yahoo.reportr.ui.view.fragment.UserFragment;
import com.yahoo.reportr.utils.Constants;

public class TopicActivity extends AppCompatActivity {

    private Topic topic;
    private String topicName;
    private TopicFragment topicFragment;
    private String authkey;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        topic = getIntent().getParcelableExtra(Constants.KEY_TOPIC);
        topicName = getIntent().getStringExtra(Constants.KEY_TOPIC_NAME);
        authkey = getIntent().getStringExtra(Constants.KEY_AUTHKEY);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Request");
        showTopicDetail();
    }

    public void showTopicDetail() {
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_TOPIC, topic);
        args.putString(Constants.KEY_TOPIC_NAME, topicName);
        args.putString(Constants.KEY_AUTHKEY, authkey);
        topicFragment = TopicFragment.newInstance(args);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.flTopic, topicFragment).commit();
    }
}

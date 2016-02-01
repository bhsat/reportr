package com.yahoo.reportr.ui.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.Topic;
import com.yahoo.reportr.ui.view.fragment.BlogFragment;
import com.yahoo.reportr.ui.view.fragment.TopicFragment;
import com.yahoo.reportr.utils.Constants;

public class BlogListActivity extends AppCompatActivity {

    private Topic topic;
    private String authkey;
    private BlogFragment blogFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        topic = getIntent().getParcelableExtra(Constants.KEY_TOPIC);
        authkey = getIntent().getStringExtra(Constants.KEY_AUTHKEY);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Select Tumblr");
        showBlogs();
    }

    public void showBlogs() {
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_TOPIC, topic);
        args.putString(Constants.KEY_AUTHKEY, authkey);
        blogFragment = BlogFragment.newInstance(args);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.flBlogs, blogFragment).commit();
    }
}

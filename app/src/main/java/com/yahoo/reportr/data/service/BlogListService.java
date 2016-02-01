package com.yahoo.reportr.data.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;

import com.yahoo.reportr.data.entity.Blog;
import com.yahoo.reportr.data.entity.Post;
import com.yahoo.reportr.ui.presenter.BlogsPresenter;
import com.yahoo.reportr.ui.view.fragment.BlogFragment;
import com.yahoo.reportr.utils.Constants;
import com.yahoo.reportr.utils.MemCache;

import java.util.List;

/**
 * Created by bhavanis on 1/25/16.
 */
public class BlogListService extends IntentService {
    public BlogListService() {
        super("blogs-service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        BlogsPresenter presenter = new BlogsPresenter();
        String authkey = intent.getStringExtra(Constants.KEY_AUTHKEY);
        //List<Blog> blogs =  presenter.getBlogs(authkey);
        //MemCache.getInstance().getLru().put(Constants.BLOG_LIST, blogs);
        List<Blog> blogs = (List<Blog>) MemCache.getInstance().getLru().get(Constants.BLOG_LIST);
        if (blogs != null) {
            for (Blog blog : blogs) {
                List<Post> posts = presenter.getBlogPosts(authkey, blog.getName());
                MemCache.getInstance().getLru().put(blog.getName(), posts);
                System.out.println("Saved posts under blogname " + blog.getName());
            }
        }
    }
}

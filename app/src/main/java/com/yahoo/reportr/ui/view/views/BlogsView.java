package com.yahoo.reportr.ui.view.views;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import com.yahoo.reportr.data.entity.Blog;
import com.yahoo.reportr.data.entity.Post;

import java.util.List;

/**
 * Created by bhavanis on 1/12/16.
 */
public interface BlogsView {

    @UiThread
    void addBlogs(@NonNull List<Blog> blogs);

    @UiThread
    void addPosts(@NonNull List<Post> posts);
}

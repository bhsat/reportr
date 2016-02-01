package com.yahoo.reportr.ui.presenter;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yahoo.reportr.data.entity.ApiResponse;
import com.yahoo.reportr.data.entity.Blog;
import com.yahoo.reportr.data.entity.Post;
import com.yahoo.reportr.domain.interactor.ReportrInteractor;
import com.yahoo.reportr.ui.DaggerReportrComponent;
import com.yahoo.reportr.ui.view.views.BlogsView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by bhavanis on 1/13/16.
 */
public class BlogsPresenter {

    private static final String TAG = BlogsPresenter.class.getSimpleName();
    private static final int HTTP_OK = 200;
    @Inject
    ReportrInteractor reportrInteractor;

    private Gson json;
    private JsonParser jsonParser;
    private List<Blog> blogList;
    private List<Post> postList;

    public BlogsPresenter() {
        DaggerReportrComponent.builder().build().inject(this);
        json = new Gson();
        jsonParser = new JsonParser();
    }

    public List<Blog> getBlogs(String authkey) {
        blogList = new ArrayList<>();
        Call<ApiResponse> call = reportrInteractor.getBlogs(authkey);
        try {
            Response<ApiResponse> response = call.execute();
            if (response.code() == HTTP_OK) {
                blogList.addAll(handleBlogs(response.body()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return blogList;
    }

    public List<Post> getBlogPosts(String authkey, String name) {
        postList = new ArrayList<>();
        Call<ApiResponse> call = reportrInteractor.getBlogPosts(authkey, name);
        try {
            Response<ApiResponse> response = call.execute();
            if (response.code() == HTTP_OK) {
                postList.addAll(handleBlogPosts(response.body()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return postList;
    }

    private List<Post> handleBlogPosts(ApiResponse data) {
        List<Post> posts = new ArrayList<>();
        String result = json.toJson(data.getData());
        JsonArray postsArr = jsonParser.parse(result).getAsJsonArray();
        if (postsArr != null) {
            for (JsonElement element : postsArr) {
                Post post = new Post();
                JsonObject obj = element.getAsJsonObject();
                if (obj != null) {
                    if (obj.has("id")) {
                        post.setId(obj.get("id").getAsLong());
                    }
                    if (obj.has("type")) {
                        post.setType(obj.get("type").getAsString());
                    }
                    if (obj.has("state")) {
                        post.setState(obj.get("state").getAsString());
                    }
                    if (obj.has("title")) {
                        post.setTitle(obj.get("title").getAsString());
                    }
                    if (obj.has("blogName")) {
                        post.setBlogName(obj.get("blogName").getAsString());
                    }
                    if (obj.has("postUrl")) {
                        post.setPostUrl(obj.get("postUrl").getAsString());
                    }
                    if (obj.has("body")) {
                        post.setBody(obj.get("body").getAsString());
                    }
                    if (obj.has("timestamp")) {
                        post.setTimestamp(obj.get("timestamp").getAsLong());
                    }
                }
                posts.add(post);
            }
        }
        return posts;
    }

    private List<Blog> handleBlogs(ApiResponse data) {
        String result = json.toJson(data.getData());
        JsonObject obj = jsonParser.parse(result).getAsJsonObject();
        List<Blog> blogList = new ArrayList<>();
        if (obj != null) {
            if (obj.has("blogs")) {
                JsonArray blogsArr = obj.getAsJsonArray("blogs");
                for (JsonElement blogElement : blogsArr) {
                    Blog blog = new Blog();
                    JsonObject jsonObj = blogElement.getAsJsonObject();
                    if (jsonObj != null) {
                        if (jsonObj.has("name")) {
                            blog.setName(jsonObj.get("name").getAsString());
                        }
                        if (jsonObj.has("title")) {
                            blog.setTitle(jsonObj.get("title").getAsString());
                        }
                        if (jsonObj.has("description")) {
                            blog.setDescription(jsonObj.get("description").getAsString());
                        }
                        if (jsonObj.has("postCount")) {
                            blog.setPostCount(jsonObj.get("postCount").getAsInt());
                        }
                    }
                    blogList.add(blog);
                }
            }
        }
        return blogList;
    }
}

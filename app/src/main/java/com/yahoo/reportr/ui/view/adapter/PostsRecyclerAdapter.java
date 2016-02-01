package com.yahoo.reportr.ui.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.Blog;
import com.yahoo.reportr.data.entity.Post;
import com.yahoo.reportr.data.entity.Topic;
import com.yahoo.reportr.ui.view.activity.PostListActivity;
import com.yahoo.reportr.ui.view.activity.SubmissionActivity;
import com.yahoo.reportr.utils.CircleTransform;
import com.yahoo.reportr.utils.Constants;

/**
 * Created by bhavanis on 1/8/16.
 */
public class PostsRecyclerAdapter extends RecyclerView.Adapter<PostsRecyclerAdapter.BlogItemViewHolder> {

    private List<Post> posts;
    private String authkey;
    private Topic topic;
    private String blogName;

    public PostsRecyclerAdapter(String authkey, Topic topic, String blogName) {
        this.posts = new ArrayList<>();
        this.authkey = authkey;
        this.topic = topic;
        this.blogName = blogName;
    }

    @Override
    public BlogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fragment_postitem, parent, false);
        return new BlogItemViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(BlogItemViewHolder holder, int position) {
        Post post = posts.get(position);
        Glide.with(holder.context).load("https://api.tumblr.com/v2/blog/"+blogName+".tumblr.com/avatar/96")
                .transform(new CircleTransform(holder.context)).into(holder.ivPostPic);
        holder.tvPostTitle.setText(post.getTitle());
        holder.tvPostBody.setText(post.getBody().substring(0,10));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class BlogItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivPostPic;
        public Context context;
        public TextView tvPostTitle;
        public TextView tvPostBody;

        public BlogItemViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(this);
            ivPostPic = (ImageView) itemView.findViewById(R.id.ivPostPic);
            tvPostTitle = (TextView) itemView.findViewById(R.id.tvPostTitle);
            tvPostBody = (TextView) itemView.findViewById(R.id.tvPostBody);
        }

        @Override
        public void onClick(View v) {
            int position = getPosition();
            Post post = posts.get(position);
            Intent intent = new Intent(context, SubmissionActivity.class);
            intent.putExtra(Constants.KEY_TOPIC, topic);
            intent.putExtra(Constants.KEY_AUTHKEY, authkey);
            intent.putExtra(Constants.KEY_BLOG_NAME, blogName);
            intent.putExtra(Constants.KEY_POST, post);
            context.startActivity(intent);
        }
    }

    public void addAll(List<Post> postList) {
        posts.addAll(postList);
        notifyDataSetChanged();
    }
}

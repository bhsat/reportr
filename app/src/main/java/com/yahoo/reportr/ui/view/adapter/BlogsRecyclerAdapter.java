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
import com.yahoo.reportr.data.entity.Topic;
import com.yahoo.reportr.ui.view.activity.PostListActivity;
import com.yahoo.reportr.utils.CircleTransform;
import com.yahoo.reportr.utils.Constants;

/**
 * Created by bhavanis on 1/8/16.
 */
public class BlogsRecyclerAdapter extends RecyclerView.Adapter<BlogsRecyclerAdapter.BlogItemViewHolder> {

    private List<Blog> blogs;
    private String authkey;
    private Topic topic;

    public BlogsRecyclerAdapter(String authkey, Topic topic) {
        this.blogs = new ArrayList<>();
        this.authkey = authkey;
        this.topic = topic;
    }

    @Override
    public BlogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fragment_blogitem, parent, false);
        return new BlogItemViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(BlogItemViewHolder holder, int position) {
        Blog blog = blogs.get(position);
        Glide.with(holder.context).load("https://api.tumblr.com/v2/blog/"+blog.getName()+".tumblr.com/avatar/96")
                .transform(new CircleTransform(holder.context)).into(holder.ivProfile);
        holder.tvUsername.setText(blog.getName());
        holder.tvTitle.setText(blog.getTitle());
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }

    public class BlogItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivProfile;
        public Context context;
        public TextView tvUsername;
        public TextView tvTitle;
        public ImageView ivRight;

        public BlogItemViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(this);
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvTitle = (TextView) itemView.findViewById(R.id.tvPostTitle);
            ivRight = (ImageView) itemView.findViewById(R.id.ivRightArrow);
        }

        @Override
        public void onClick(View v) {
            int position = getPosition();
            Blog blog = blogs.get(position);
            Intent intent = new Intent(context, PostListActivity.class);
            intent.putExtra(Constants.KEY_BLOG, blog);
            intent.putExtra(Constants.KEY_TOPIC, topic);
            intent.putExtra(Constants.KEY_AUTHKEY, authkey);
            context.startActivity(intent);
        }
    }

    public void addAll(List<Blog> blogList) {
        blogs.addAll(blogList);
        notifyDataSetChanged();
    }
}

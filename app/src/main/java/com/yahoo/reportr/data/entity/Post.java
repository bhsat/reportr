package com.yahoo.reportr.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bhavanis on 1/12/16.
 */
public class Post implements Parcelable {

    private Long id;
    private String type;
    private String state;
    private String title;
    private String blogName;
    private String postUrl;
    private String body;
    private Long timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.type);
        dest.writeString(this.state);
        dest.writeString(this.title);
        dest.writeString(this.blogName);
        dest.writeString(this.postUrl);
        dest.writeString(this.body);
        dest.writeValue(this.timestamp);
    }

    public Post() {
    }

    protected Post(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.type = in.readString();
        this.state = in.readString();
        this.title = in.readString();
        this.blogName = in.readString();
        this.postUrl = in.readString();
        this.body = in.readString();
        this.timestamp = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}

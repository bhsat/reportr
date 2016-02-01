package com.yahoo.reportr.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bhavanis on 1/12/16.
 */
public class Blog implements Parcelable {

    private String name;
    private String title;
    private String description;
    private Integer postCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPostCount() {
        return postCount;
    }

    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeValue(this.postCount);
    }

    public Blog() {
    }

    protected Blog(Parcel in) {
        this.name = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.postCount = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Blog> CREATOR = new Parcelable.Creator<Blog>() {
        public Blog createFromParcel(Parcel source) {
            return new Blog(source);
        }

        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };

    @Override
    public String toString() {
        return "Blog{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", postCount=" + postCount +
                '}';
    }
}

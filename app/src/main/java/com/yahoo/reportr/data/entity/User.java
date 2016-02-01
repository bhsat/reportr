package com.yahoo.reportr.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavanis on 1/7/16.
 */
public class User implements Parcelable {
    private String emailId;

    private Integer balance;

    private List<Integer> ontologies;

    private List<Topic> topics;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public List<Integer> getOntologies() {
        return ontologies;
    }

    public void setOntologies(List<Integer> ontologies) {
        this.ontologies = ontologies;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.emailId);
        dest.writeValue(this.balance);
        dest.writeList(this.ontologies);
        dest.writeTypedList(topics);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.emailId = in.readString();
        this.balance = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ontologies = new ArrayList<Integer>();
        in.readList(this.ontologies, List.class.getClassLoader());
        this.topics = in.createTypedArrayList(Topic.CREATOR);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

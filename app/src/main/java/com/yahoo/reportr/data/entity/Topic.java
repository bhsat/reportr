package com.yahoo.reportr.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Set;

/**
 * Created by bhavanis on 1/7/16.
 */
public class Topic implements Parcelable {
    private Integer id;
    private Date createDate;
    private String summary;
    private String description;
    private Date embargoUntil;
    private Date expires;
    private Integer amount;
    private boolean active;
    private Set<Integer> ontologies;
    private Integer submissionsCount;
    private Boolean paid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEmbargoUntil() {
        return embargoUntil;
    }

    public void setEmbargoUntil(Date embargoUntil) {
        this.embargoUntil = embargoUntil;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Set<Integer> getOntologies() {
        return ontologies;
    }

    public void setOntologies(Set<Integer> ontologies) {
        this.ontologies = ontologies;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getSubmissionsCount() {
        return submissionsCount;
    }

    public void setSubmissionsCount(Integer submissionsCount) {
        this.submissionsCount = submissionsCount;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeLong(createDate != null ? createDate.getTime() : -1);
        dest.writeString(this.summary);
        dest.writeString(this.description);
        dest.writeLong(expires != null ? expires.getTime() : -1);
        dest.writeValue(this.amount);
    }

    public Topic() {
    }

    protected Topic(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        long tmpCreateDate = in.readLong();
        this.createDate = tmpCreateDate == -1 ? null : new Date(tmpCreateDate);
        this.summary = in.readString();
        this.description = in.readString();
        long tmpExpires = in.readLong();
        this.expires = tmpExpires == -1 ? null : new Date(tmpExpires);
        this.amount = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Topic> CREATOR = new Parcelable.Creator<Topic>() {
        public Topic createFromParcel(Parcel source) {
            return new Topic(source);
        }

        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };
}

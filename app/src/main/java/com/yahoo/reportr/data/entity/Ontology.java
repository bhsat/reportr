package com.yahoo.reportr.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.SortedSet;

/**
 * Created by bhavanis on 12/16/15.
 */
public class Ontology implements Comparable<Ontology>,Parcelable {

    private String name;
    private int id;
    private SortedSet<Ontology> kids;
    private transient int sortOrder;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SortedSet<Ontology> getKids() {
        return kids;
    }

    public void setKids(SortedSet<Ontology> kids) {
        this.kids = kids;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public int compareTo(Ontology o) {
        return this.sortOrder - o.sortOrder;
    }

    @Override
    public String toString() {
        return "Ontology{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", kids=" + kids +
                ", sortOrder=" + sortOrder +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.id);
        dest.writeInt(this.sortOrder);
    }

    public Ontology() {
    }

    protected Ontology(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
        this.sortOrder = in.readInt();
    }

    public static final Parcelable.Creator<Ontology> CREATOR = new Parcelable.Creator<Ontology>() {
        public Ontology createFromParcel(Parcel source) {
            return new Ontology(source);
        }

        public Ontology[] newArray(int size) {
            return new Ontology[size];
        }
    };
}

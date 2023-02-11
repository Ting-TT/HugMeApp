package com.example.Posts;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author: Xie Hu, Xitong Liu
 */

/**
 * This class implements interface Parcelable to support intent put extra object<PostAll>.
 */
public class PostAll implements Parcelable {
    private String key;
    private PostEntity pe;

    public PostAll(String key, PostEntity pe) {
        this.key = key;
        this.pe = pe;
    }

    public PostAll() {}

    protected PostAll(Parcel in) {
        key = in.readString();
    }

    public static final Creator<PostAll> CREATOR = new Creator<PostAll>() {
        @Override
        public PostAll createFromParcel(Parcel in) {
            return new PostAll(in);
        }

        @Override
        public PostAll[] newArray(int size) {
            return new PostAll[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public PostEntity getPe() {
        return pe;
    }

    public void setPe(PostEntity pe) {
        this.pe = pe;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
    }
}

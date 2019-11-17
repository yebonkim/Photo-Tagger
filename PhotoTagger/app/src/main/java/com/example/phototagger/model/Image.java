package com.example.phototagger.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class Image implements Parcelable{
    private int mId;
    private String mTitle;
    private String mLocation;
    private String mWidth;
    private String mHeight;
    private ArrayList<String> mTags;

    public Image(String location, String width, String height) {
        this.mLocation = location;
        this.mWidth = width;
        this.mHeight = height;
        this.mTags = new ArrayList<>();
    }

    protected Image(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mLocation = in.readString();
        mWidth = in.readString();
        mHeight = in.readString();
        mTags = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mLocation);
        dest.writeString(mWidth);
        dest.writeString(mHeight);
        dest.writeStringList(mTags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public String getWidth() {
        return mWidth;
    }

    public void setWidth(String width) {
        this.mWidth = width;
    }

    public String getHeight() {
        return mHeight;
    }

    public void setHeight(String height) {
        this.mHeight = height;
    }

    public ArrayList<String> getTag() {
        return mTags;
    }

    public void setTag(ArrayList<String> tag) {
        this.mTags = tag;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }
}
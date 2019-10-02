package com.example.phototagger.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class Image implements Parcelable{
    private int id;
    private String title;
    private String location;
    private String width;
    private String height;
    private ArrayList<String> tag;

    public Image(String location, String width, String height) {
        this.location = location;
        this.width = width;
        this.height = height;
        this.tag = new ArrayList<>();
    }

    protected Image(Parcel in) {
        id = in.readInt();
        title = in.readString();
        location = in.readString();
        width = in.readString();
        height = in.readString();
        tag = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(location);
        dest.writeString(width);
        dest.writeString(height);
        dest.writeStringList(tag);
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
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
package com.example.phototagger.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class Gallery implements Parcelable{
    private String mTitle;
    private ArrayList<Image> mImages;

    public Gallery(String title) {
        this.mTitle = title;
        this.mImages = new ArrayList<>();
    }

    protected Gallery(Parcel in) {
        mTitle = in.readString();
        mImages = in.createTypedArrayList(Image.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeTypedList(mImages);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Gallery> CREATOR = new Creator<Gallery>() {
        @Override
        public Gallery createFromParcel(Parcel in) {
            return new Gallery(in);
        }

        @Override
        public Gallery[] newArray(int size) {
            return new Gallery[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public ArrayList<Image> getImages() {
        return mImages;
    }

    public void setImages(ArrayList<Image> images) {
        this.mImages = images;
    }

    public void addImage(Image image) {
        this.mImages.add(image);
    }
}
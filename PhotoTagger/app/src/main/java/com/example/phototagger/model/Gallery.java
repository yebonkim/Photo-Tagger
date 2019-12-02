package com.example.phototagger.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class Gallery implements Parcelable{
    private String mTitle;
    private boolean mIsOpened;
    private ArrayList<Image> mImages;

    public Gallery(String title, boolean isOpened) {
        this.mTitle = title;
        this.mIsOpened = isOpened;
        this.mImages = new ArrayList<>();
    }

    protected Gallery(Parcel in) {
        mTitle = in.readString();
        mIsOpened = in.readByte() != 0;
        mImages = in.createTypedArrayList(Image.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeByte((byte) (mIsOpened ? 1 : 0));
        dest.writeTypedList(mImages);
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

    @Override
    public int describeContents() {
        return 0;
    }

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

    public boolean isOpened() {
        return mIsOpened;
    }

    public void setIsOpened(boolean isOpened) {
        this.mIsOpened = isOpened;
    }
}
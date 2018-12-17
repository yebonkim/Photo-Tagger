package com.example.phototagger.main;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phototagger.R;
import com.example.phototagger.RecyclerViewHolder;
import com.example.phototagger.model.Gallery;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_GALLERY = 1;

    Activity activity;
    ArrayList<Gallery> galleries;

    public GalleryAdapter(Activity activity, ArrayList<Gallery> galleries) {
        setHasStableIds(true);

        this.activity = activity;
        this.galleries = galleries;
    }

    @Override
    public int getItemViewType(int position) {
            return VIEW_TYPE_GALLERY;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view;
        switch (viewType) {
            case VIEW_TYPE_GALLERY:
                view = LayoutInflater.from(context).inflate(R.layout.viewholder_gallery, parent, false);
                return new GalleryView(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((RecyclerViewHolder)holder).setData(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    };

    @Override
    public int getItemCount() {
        return galleries.size();
    }

    public class GalleryView extends RecyclerView.ViewHolder implements RecyclerViewHolder{
        @BindView(R.id.galleryNameTV)
        TextView galleryNameTV;
        @BindView(R.id.galleryRV)
        RecyclerView galleryRV;

        View view;
        Context context;
        ImageAdapter adapter;

        public GalleryView(View view) {
            super(view);
            this.view = view;
            context =view.getContext();
            ButterKnife.bind(this, view);
        }

        @Override
        public void setData(int position) {
            adapter = new ImageAdapter(activity, galleries.get(position));
            galleryRV.setLayoutManager(new GridLayoutManager(context, 3));
            galleryRV.setAdapter(adapter);
            galleryNameTV.setText(galleries.get(position).getTitle());
        }
    }
}
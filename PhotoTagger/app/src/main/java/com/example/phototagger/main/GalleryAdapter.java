package com.example.phototagger.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
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

import java.lang.invoke.ConstantCallSite;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_GALLERY = 1;

    ArrayList<Gallery> mGalleries;

    public GalleryAdapter(ArrayList<Gallery> galleries) {
        setHasStableIds(true);

        this.mGalleries = galleries;
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
        return mGalleries.size();
    }

    public class GalleryView extends RecyclerView.ViewHolder implements RecyclerViewHolder{
        @BindView(R.id.text_gallery_name)
        TextView mGalleryName;
        @BindView(R.id.list_gallery)
        RecyclerView mGalleryList;
        @BindView(R.id.layout_title)
        ConstraintLayout mTitleLayout;
        @BindView(R.id.image_arrow)
        ImageView mArrowImage;

        private Context mContext;
        private ImageAdapter mAdapter;
        private Drawable upDrawable, downDrawable;
        private Gallery mGallery;

        public GalleryView(View view) {
            super(view);
            mContext =view.getContext();
            ButterKnife.bind(this, view);

            upDrawable = mContext.getResources().getDrawable(R.drawable.ic_up);
            downDrawable = mContext.getResources().getDrawable(R.drawable.ic_down);
            mGalleryList.setLayoutManager(new GridLayoutManager(mContext, 3));
        }

        @Override
        public void setData(int position) {
            mGallery = mGalleries.get(position);
            mAdapter = new ImageAdapter(mGallery);

            if (mGallery.isOpened()) {
                setOpenedView();
            } else {
                setClosedView();
            }

            mGalleryList.setAdapter(mAdapter);
            mGalleryName.setText(mGalleries.get(position).getTitle());
            mTitleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mGallery.isOpened()) {
                        setClosedView();
                    } else {
                        setOpenedView();
                    }

                    mGallery.setIsOpened(!mGallery.isOpened());
                }
            });
        }

        private void setOpenedView() {
            mGalleryList.setVisibility(View.VISIBLE);
            mArrowImage.setBackground(downDrawable);
        }

        private void setClosedView() {
            mGalleryList.setVisibility(View.GONE);
            mArrowImage.setBackground(upDrawable);
        }
    }
}
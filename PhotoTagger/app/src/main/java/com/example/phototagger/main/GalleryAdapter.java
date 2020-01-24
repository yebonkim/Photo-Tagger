package com.example.phototagger.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phototagger.EndlessScrollListener;
import com.example.phototagger.R;
import com.example.phototagger.RecyclerViewHolder;
import com.example.phototagger.common.IntentConstant;
import com.example.phototagger.detail.DetailActivity;
import com.example.phototagger.model.Gallery;
import com.example.phototagger.model.Image;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        private static final int GRID_SPAN_CNT = 3;

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
        private GridLayoutManager mGridLayoutManager;
        private EndlessScrollListener mEndlessScrollListener;

        private Drawable upDrawable, downDrawable;
        private Gallery mGallery;

        public GalleryView(View view) {
            super(view);
            mContext =view.getContext();
            ButterKnife.bind(this, view);

            upDrawable = mContext.getResources().getDrawable(R.drawable.ic_up);
            downDrawable = mContext.getResources().getDrawable(R.drawable.ic_down);

            mGridLayoutManager = new GridLayoutManager(mContext, GRID_SPAN_CNT);
            mGalleryList.setLayoutManager(mGridLayoutManager);
        }

        @Override
        public void setData(int position) {
            mGallery = mGalleries.get(position);
            setAdapter();

            if (mGallery.isOpened()) {
                setOpenedView();
            } else {
                setClosedView();
            }

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

        @Nullable
        private List<Image> getImages(int fromIdx, int cnt) {
            List<Image> results = new ArrayList<>();
            int toIdx = fromIdx + cnt;
            int totalCnt = mGallery.getImages().size();

            if (totalCnt <= fromIdx) {
                return null;
            }
            if (toIdx > totalCnt) {
                toIdx = totalCnt;
            }

            for(int i = fromIdx; i < toIdx; i++) {
                results.add(mGallery.getImages().get(i));
            }

            return results;
        }

        private void setAdapter() {
            mEndlessScrollListener = new EndlessScrollListener(mGridLayoutManager) {
                @Override
                public void onLoadMore(int totalItemsCount, int visibleThreshold, RecyclerView view) {
                    final List<Image> newImages = getImages(totalItemsCount, visibleThreshold);

                    if (newImages == null || mAdapter == null) {
                        return;
                    }

                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addImagesToGallery(newImages);
                        }
                    });
                }
            };

            List<Image> initialImage = getImages(0, mEndlessScrollListener.getVisibleThreshold());
            if (initialImage == null) {
                return;
            }

            mAdapter = new ImageAdapter(initialImage, mOnPhotoClickListener);
            mGalleryList.setAdapter(mAdapter);
            mGalleryList.setOnScrollListener(mEndlessScrollListener);
        }

        private OnPhotoClickListener mOnPhotoClickListener = new OnPhotoClickListener() {
            @Override
            public void onPhotoClicked(int imagePosition) {
                Intent i = new Intent(mContext, DetailActivity.class);
                i.putExtra(IntentConstant.GALLERY, mGallery);
                i.putExtra(IntentConstant.IMAGE_POSITION, imagePosition);
                mContext.startActivity(i);
            }
        };

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
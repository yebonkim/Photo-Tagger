package com.example.phototagger.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.phototagger.R;
import com.example.phototagger.RecyclerViewHolder;
import com.example.phototagger.common.IntentConstant;
import com.example.phototagger.common.S3Utils;
import com.example.phototagger.detail.DetailActivity;
import com.example.phototagger.model.Gallery;
import com.example.phototagger.model.Image;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_IMAGE = 1;

    Activity activity;
    Gallery gallery;

    public ImageAdapter(Activity activity, Gallery gallery) {
        setHasStableIds(true);

        this.activity = activity;
        this.gallery = gallery;
    }

    @Override
    public int getItemViewType(int position) {
            return VIEW_TYPE_IMAGE;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view;
        switch (viewType) {
            case VIEW_TYPE_IMAGE:
                view = LayoutInflater.from(context).inflate(R.layout.viewholder_image, parent, false);
                return new MyImageView(view);
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
        return gallery.getImages().size();
    }

    public class MyImageView extends RecyclerView.ViewHolder implements RecyclerViewHolder{
        @BindView(R.id.imageV)
        ImageView imageV;
        @BindView(R.id.nameTV)
        TextView nameTV;
        @BindView(R.id.checkbox)
        CheckBox checkBox;

        View view;
        Context mContext;

        public MyImageView(View view) {
            super(view);
            this.view = view;
            mContext =view.getContext();
            ButterKnife.bind(this, view);
        }

        @Override
        public void setData(final int position) {
            nameTV.setText(gallery.getImages().get(position).getTitle());
            Glide.with(mContext).load(gallery.getImages().get(position).getLocation()).into(imageV);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, DetailActivity.class);
                    i.putExtra(IntentConstant.GALLERY, gallery);
                    i.putExtra(IntentConstant.IMAGE_POSITION, position);
                    activity.startActivity(i);
                    activity.finish();
                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        S3Utils.uploadFile(mContext, new File(gallery.getImages().get(position).getLocation()));
                    } else {
                        S3Utils.deleteFile(new File(gallery.getImages().get(position).getLocation()));
                    }
                }
            });
        }
    }
}
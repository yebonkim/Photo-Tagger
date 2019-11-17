package com.example.phototagger.slide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.phototagger.R;
import com.example.phototagger.common.IntentConstant;
import com.example.phototagger.model.Image;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by yebonkim on 15/12/2018.
 *
 */
public class SlideFragment extends Fragment {
    @BindView(R.id.image)
    ImageView mImage;

    Image mData;

    public SlideFragment() {
    }

    /**
     * @param image image to show.
     * @return A new instance of fragment SlideFragment.
     */
    public static SlideFragment newInstance(Image image) {
        SlideFragment fragment = new SlideFragment();
        Bundle args = new Bundle();
        args.putParcelable(IntentConstant.IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mData = getArguments().getParcelable(IntentConstant.IMAGE);
        } else {
            mData = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide, container, false);
        ButterKnife.bind(this, view);

        if(mData == null) {
            getActivity().finish();
        }

        setView();

        return view;
    }

    protected void setView() {
        Glide.with(getContext()).load(mData.getLocation()).into(mImage);
    }

}

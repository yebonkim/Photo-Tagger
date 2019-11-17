package com.example.phototagger.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.phototagger.R;
import com.example.phototagger.common.IntentConstant;
import com.example.phototagger.model.Image;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by yebonkim on 15/12/2018.
 *
 */
public class DetailFragment extends Fragment {
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.text_name)
    TextView mNameText;
    @BindView(R.id.text_location)
    TextView mLocationText;
    @BindView(R.id.text_size)
    TextView mSizeText;
    @BindView(R.id.text_tag)
    TextView mTagText;

    private Image mData;

    public DetailFragment() { }

    /**
     * @param image image to show.
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(Image image) {
        DetailFragment fragment = new DetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        if(mData == null) {
            getActivity().finish();
        }

        setView();

        return view;
    }

    protected void setView() {
        Glide.with(getContext()).load(mData.getLocation()).into(mImage);

        mNameText.setText(mData.getTitle());
        mLocationText.setText(mData.getLocation());
        mSizeText.setText(mData.getWidth() + " * " + mData.getHeight());
        mTagText.setText(getTagString(mData.getTag()));
    }

    protected String getTagString(ArrayList<String> tag) {
        String result = "";

        for(String tagStr : tag) {
            result += "#" + tagStr + ", ";
        }

        return result;
    }

}

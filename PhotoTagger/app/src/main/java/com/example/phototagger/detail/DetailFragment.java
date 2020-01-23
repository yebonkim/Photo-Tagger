package com.example.phototagger.detail;

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
import com.example.phototagger.server.EsQueryResponse;
import com.example.phototagger.server.ServerQuery;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        getTagFromEs();
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

    private void getTagFromEs() {
        ServerQuery.queryToES(mData.getTitle(), new Callback<EsQueryResponse>() {
            @Override
            public void onResponse(Call<EsQueryResponse> call, Response<EsQueryResponse> response) {
                if (response != null && response.body() != null) {
                    EsQueryResponse queryResponse = response.body();
                    EsQueryResponse.InnerHits hit = queryResponse.getHits().getMaxScoreHit();

                    if (hit != null) {
                        setTagText(formatTagString(hit.getSource().getLabels()));
                    }
                }
            }

            @Override
            public void onFailure(Call<EsQueryResponse> call, Throwable t) {

            }
        });
    }

    protected void setView() {
        Glide.with(getContext()).load(mData.getLocation()).into(mImage);

        mNameText.setText(mData.getTitle());
        mLocationText.setText(mData.getLocation());
        mSizeText.setText(mData.getWidth() + " * " + mData.getHeight());
    }

    protected void setTagText(String tags) {
        mTagText.setText(tags);
    }

    protected String formatTagString(List<String> tag) {
        StringBuilder builder = new StringBuilder();

        if (tag.size() == 0) {
            return builder.toString();
        }

        for(String tagStr : tag) {
            builder.append("#");
            builder.append(tagStr);
            builder.append(", ");
        }

        return builder.substring(0, builder.length() - 2);
    }

}

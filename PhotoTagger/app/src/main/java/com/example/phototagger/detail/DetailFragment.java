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
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    @BindView(R.id.imageV)
    ImageView imageV;
    @BindView(R.id.nameTV)
    TextView nameTV;
    @BindView(R.id.locationTV)
    TextView locationTV;
    @BindView(R.id.sizeTV)
    TextView sizeTV;
    @BindView(R.id.tagTV)
    TextView tagTV;
    Image image;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param image Parameter 1.
     * @return A new instance of fragment FragmentQuiz.
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
            image = getArguments().getParcelable(IntentConstant.IMAGE);
        } else {
            image = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        if(image == null)
            getActivity().finish();

        setView();

        return view;
    }

    protected void setView() {
        Glide.with(getContext()).load(image.getLocation()).into(imageV);
        nameTV.setText(image.getTitle());
        locationTV.setText(image.getLocation());
        sizeTV.setText(image.getWidth()+" * "+image.getHeight());
        tagTV.setText(getTagString(image.getTag()));
    }

    protected String getTagString(ArrayList<String> tag) {
        String result = "";

        for(String tagStr : tag) {
            result += "#"+tagStr+", ";
        }

        return result;
    }

}

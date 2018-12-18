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
 * A simple {@link Fragment} subclass.
 * Use the {@link SlideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlideFragment extends Fragment {
    @BindView(R.id.imageV)
    ImageView imageV;
    Image image;

    public SlideFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param image Parameter 1.
     * @return A new instance of fragment FragmentQuiz.
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
            image = getArguments().getParcelable(IntentConstant.IMAGE);
        } else {
            image = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slide, container, false);
        ButterKnife.bind(this, view);

        if(image == null)
            getActivity().finish();

        setView();

        return view;
    }

    protected void setView() {
        Glide.with(getContext()).load(image.getLocation()).into(imageV);
    }

}

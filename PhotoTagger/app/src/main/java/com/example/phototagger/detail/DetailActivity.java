package com.example.phototagger.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.phototagger.R;
import com.example.phototagger.common.IntentConstant;
import com.example.phototagger.model.Gallery;
import com.example.phototagger.model.Image;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.button_left)
    Button mLeftBtn;
    @BindView(R.id.button_right)
    Button mRightBtn;

    MainViewPageAdapter mAdapter;

    Gallery mGallery;
    int mImageIdx;
    int mNowPageIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getIntentData();
        setViewPager();
        getSupportActionBar().setTitle(mGallery.getTitle());
    }

    protected void getIntentData() {
        mGallery = getIntent().getParcelableExtra(IntentConstant.GALLERY);
        mImageIdx = getIntent().getIntExtra(IntentConstant.IMAGE_POSITION, 0);
        mNowPageIdx = mImageIdx;
    }

    protected void setViewPager() {
        mViewPager.setClipToPadding(false);
        mViewPager.setClipChildren(false);
        mViewPager.setOffscreenPageLimit(5);
        mAdapter = new MainViewPageAdapter(getSupportFragmentManager(), prepareFragmentList());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mImageIdx);
        mViewPager.addOnPageChangeListener(pageChangeListener);
    }

    protected ArrayList<DetailFragment> prepareFragmentList() {
        ArrayList<DetailFragment> result = new ArrayList<>();

        for(Image image : mGallery.getImages()) {
            result.add(DetailFragment.newInstance(image));
        }

        return result;
    }

    @OnClick(R.id.button_left)
    public void onLeftBtnClicked() {
        if(mNowPageIdx >0 && mGallery.getImages().size()!=0) {
            mNowPageIdx--;
            mViewPager.setCurrentItem(mNowPageIdx);
        }
    }

    @OnClick(R.id.button_right)
    public void onRightBtnClicked() {
        if(mNowPageIdx < mGallery.getImages().size()-1) {
            mNowPageIdx++;
            mViewPager.setCurrentItem(mNowPageIdx);
        }
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            mNowPageIdx = position;
            //두 버튼 다 다시 활성화
            if(mNowPageIdx ==0) {
                mLeftBtn.setBackgroundResource(R.drawable.ic_pre_white);
            } else {
                mLeftBtn.setBackgroundResource(R.drawable.ic_pre_black);
            }

            if(mNowPageIdx == mGallery.getImages().size()) {
                mRightBtn.setBackgroundResource(R.drawable.ic_next_white);
            } else {
                mRightBtn.setBackgroundResource(R.drawable.ic_next_black);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}

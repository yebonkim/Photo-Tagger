package com.example.phototagger.slide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.phototagger.R;
import com.example.phototagger.common.IntentConstant;
import com.example.phototagger.model.Gallery;
import com.example.phototagger.model.Image;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class SlideActivity extends AppCompatActivity {
    private final static int[] SLIDE_TIMES = {1000, 3000, 5000};
    private final static int[] TIME_MENU_DRAWABLES = {R.drawable.ic_one, R.drawable.ic_three, R.drawable.ic_five};

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private SlideViewPageAdapter mAdapter;

    private int mSlideTimeIdx = 0;

    private Gallery mGallery;
    private int mImageIdx;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getIntentConstant();
        setViewPager();
        getSupportActionBar().setTitle(mGallery.getTitle());

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mImageIdx < mGallery.getImages().size() - 1) {
                    mImageIdx++;
                    mViewPager.setCurrentItem(mImageIdx);
                    mHandler.postDelayed(this, SLIDE_TIMES[mSlideTimeIdx]);
                }
            }
        }, SLIDE_TIMES[mSlideTimeIdx]);
    }

    private void getIntentConstant() {
        mGallery = getIntent().getParcelableExtra(IntentConstant.GALLERY);
    }

    private void setViewPager() {
        mViewPager.setClipToPadding(false);
        mViewPager.setClipChildren(false);
        mViewPager.setOffscreenPageLimit(5);
        mAdapter = new SlideViewPageAdapter(getSupportFragmentManager(), prepareFragmentList());
        mViewPager.setAdapter(mAdapter);
    }

    protected ArrayList<SlideFragment> prepareFragmentList() {
        ArrayList<SlideFragment> result = new ArrayList<>();

        for (Image image : mGallery.getImages()) {
            result.add(SlideFragment.newInstance(image));
        }

        return result;
    }

    private void goToMainActivity() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.slide_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_time:
                if (mSlideTimeIdx < SLIDE_TIMES.length - 1) {
                    mSlideTimeIdx++;
                } else {
                    mSlideTimeIdx = 0;
                }
                item.setIcon(TIME_MENU_DRAWABLES[mSlideTimeIdx]);
                return true;

            case R.id.action_cancel:
                goToMainActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

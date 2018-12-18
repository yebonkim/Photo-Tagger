package com.example.phototagger.slide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.phototagger.R;
import com.example.phototagger.common.IntentConstant;
import com.example.phototagger.detail.DetailEditActivity;
import com.example.phototagger.detail.DetailFragment;
import com.example.phototagger.detail.MainViewPageAdapter;
import com.example.phototagger.main.MainActivity;
import com.example.phototagger.model.Gallery;
import com.example.phototagger.model.Image;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class SlideActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    SlideViewPageAdapter slideViewPageAdapter;

    Gallery gallery;
    int imageIdx;
    long slideTime = 1000;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getIntentConstant();
        setViewPager();
        getSupportActionBar().setTitle(gallery.getTitle());
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(imageIdx < gallery.getImages().size()-1) {
                    imageIdx++;
                    viewPager.setCurrentItem(imageIdx);
                    mHandler.postDelayed(this, slideTime);
                }
            }
        }, slideTime);
    }

    protected void getIntentConstant() {
        gallery = getIntent().getParcelableExtra(IntentConstant.GALLERY);
    }

    protected void setViewPager() {
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(5);
        slideViewPageAdapter = new SlideViewPageAdapter(getSupportFragmentManager(), prepareFragmentList());
        viewPager.setAdapter(slideViewPageAdapter);
    }

    protected ArrayList<SlideFragment> prepareFragmentList() {
        ArrayList<SlideFragment> result = new ArrayList<>();
        SlideFragment tmp;

        for(Image image : gallery.getImages()) {
            result.add(SlideFragment.newInstance(image));
        }

        return result;
    }

    protected void goToMainActivity() {
        Intent i = new Intent(SlideActivity.this, MainActivity.class);
        startActivity(i);
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
                // User chose the "Settings" item, show the app settings UI...
                if(slideTime == 1000)
                    slideTime = 3000;
                else if(slideTime == 3000)
                    slideTime = 5000;
                else if(slideTime == 5000)
                    slideTime = 1000;
                return true;

            case R.id.action_cancel:
                // User chose the "Settings" item, show the app settings UI...
                goToMainActivity();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}

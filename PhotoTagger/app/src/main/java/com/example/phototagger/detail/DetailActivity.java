package com.example.phototagger.detail;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.leftBtn)
    Button leftBtn;
    @BindView(R.id.rightBtn)
    Button rightBtn;

    MainViewPageAdapter viewPageAdapter;

    Gallery gallery;
    int imageIdx;
    int nowPageIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getIntentConstant();
        setViewPager();
        getSupportActionBar().setTitle(gallery.getTitle());
    }

    protected void getIntentConstant() {
        gallery = getIntent().getParcelableExtra(IntentConstant.GALLERY);
        imageIdx = getIntent().getIntExtra(IntentConstant.IMAGE_POSITION, 0);
        nowPageIdx = imageIdx;
    }

    protected void setViewPager() {
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(5);
        viewPageAdapter = new MainViewPageAdapter(getSupportFragmentManager(), prepareFragmentList());
        viewPager.setAdapter(viewPageAdapter);
        viewPager.setCurrentItem(imageIdx);
        viewPager.addOnPageChangeListener(pageChangeListener);
    }

    protected ArrayList<DetailFragment> prepareFragmentList() {
        ArrayList<DetailFragment> result = new ArrayList<>();
        DetailFragment tmp;
        int idx=0;

        for(Image image : gallery.getImages()) {
            result.add(DetailFragment.newInstance(image));
        }

        return result;
    }

    @OnClick(R.id.leftBtn)
    public void onLeftBtnClicked() {
        if(nowPageIdx>0 && gallery.getImages().size()!=0) {
            nowPageIdx--;
            viewPager.setCurrentItem(nowPageIdx);
        }
    }

    @OnClick(R.id.rightBtn)
    public void onRightBtnClicked() {
        if(nowPageIdx<gallery.getImages().size()-1) {
            nowPageIdx++;
            viewPager.setCurrentItem(nowPageIdx);
        }
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            nowPageIdx = position;
            //두 버튼 다 다시 활성화
            if(nowPageIdx==0) {
                leftBtn.setBackgroundResource(R.drawable.ic_pre_white);
            } else {
                leftBtn.setBackgroundResource(R.drawable.ic_pre_black);
            }
            if(nowPageIdx == gallery.getImages().size()) {
                rightBtn.setBackgroundResource(R.drawable.ic_next_white);
            } else {
                rightBtn.setBackgroundResource(R.drawable.ic_next_black);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    protected void goToEditActivity() {
        Intent i = new Intent(DetailActivity.this, DetailEditActivity.class);
        i.putExtra(IntentConstant.GALLERY, gallery);
        i.putExtra(IntentConstant.IMAGE_POSITION, nowPageIdx);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                // User chose the "Settings" item, show the app settings UI...
                goToEditActivity();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}

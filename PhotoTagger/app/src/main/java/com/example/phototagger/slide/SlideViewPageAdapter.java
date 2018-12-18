package com.example.phototagger.slide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.phototagger.detail.DetailFragment;

import java.util.ArrayList;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class SlideViewPageAdapter extends FragmentStatePagerAdapter {

    ArrayList<SlideFragment> fragmentList;
    public SlideViewPageAdapter(FragmentManager fm, ArrayList<SlideFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        if (position < fragmentList.size()) {
            return fragmentList.get(position);
        }
        return null;
    }

    @Override
    public String getPageTitle(int position) {
        //not used
        if (position < fragmentList.size()) {
            return "";
        }
        return null;
    }
    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
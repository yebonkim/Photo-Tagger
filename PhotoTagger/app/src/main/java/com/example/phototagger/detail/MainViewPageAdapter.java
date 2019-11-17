package com.example.phototagger.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by yebonkim on 15/12/2018.
 */

public class MainViewPageAdapter extends FragmentStatePagerAdapter {
    ArrayList<DetailFragment> mFragmentList;

    public MainViewPageAdapter(FragmentManager fm, ArrayList<DetailFragment> fragmentList) {
        super(fm);
        this.mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        if (position < mFragmentList.size()) {
            return mFragmentList.get(position);
        }
        return null;
    }

    @Override
    public String getPageTitle(int position) {
        if (position < mFragmentList.size()) {
            return "";
        }
        return null;
    }
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
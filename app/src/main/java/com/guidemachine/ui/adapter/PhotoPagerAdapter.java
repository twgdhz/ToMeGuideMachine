package com.guidemachine.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.guidemachine.ui.activity.fragment.PhotoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zheng on 2017/11/27.
 */

public class PhotoPagerAdapter extends FragmentPagerAdapter {

    private final List<String> urlList;

    public PhotoPagerAdapter(FragmentManager fm, List<String> urlList) {
        super(fm);
        this.urlList=urlList;
    }

    @Override
    public Fragment getItem(int position) {
        return PhotoFragment.newInstance(urlList.get(position));
    }

    @Override
    public int getCount() {
        return urlList.size();
    }
}

package com.guidemachine.ui.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.ui.adapter.PhotoPagerAdapter;
import com.guidemachine.util.Logger;
import com.guidemachine.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowBigImageActivity extends BaseActivity {
    private ViewPager viewPager;
    private TextView tvNum;
    private ArrayList<String> urlList;
    String imageUrl;
    List<String> data = new ArrayList<>();
    @Override
    protected int setRootViewId() {
        return R.layout.activity_show_big_image;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        imageUrl = getIntent().getExtras().getString("imageUrl");
        String[] line = imageUrl.split(",");
        for (String s : line) {
            data.add(s);
            Logger.d("相册", s.toString());
        }
        initParam();
        initView();

    }

    private void initView() {
        StatusBarUtils.setWindowStatusBarColor(ShowBigImageActivity.this, R.color.white);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tvNum = (TextView) findViewById(R.id.tv_num);
        PhotoPagerAdapter viewPagerAdapter = new PhotoPagerAdapter(getSupportFragmentManager(), data);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tvNum.setText(String.valueOf(position + 1) + "/" + data.size());
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initParam() {
//        //需要加载的网络图片
//        String[] urls = {
//                "http://p1.pstatp.com/large/pgc-image/886a0ad17c4a42048ed2269f4322886d",
//                "http://b.hiphotos.baidu.com/image/pic/item/5243fbf2b2119313999ff97a6c380cd790238d1f.jpg",
//                "http://f.hiphotos.baidu.com/image/pic/item/43a7d933c895d1430055e4e97af082025baf07dc.jpg"
//        };
//
//        urlList = new ArrayList<>();
//        Collections.addAll(urlList, urls);
    }

}

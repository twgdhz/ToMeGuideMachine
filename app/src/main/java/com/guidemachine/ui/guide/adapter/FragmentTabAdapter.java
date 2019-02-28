package com.guidemachine.ui.guide.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

/**
 * Created by Public on 2016/9/12.
 */
public class FragmentTabAdapter implements RadioGroup.OnCheckedChangeListener {
    //视图显示view
    public static List<Fragment> fragments;
    //按钮
    private RadioGroup radioGroup;
    //调用试图的activity
    private FragmentActivity fragmentActivity;
    //包含fragment视图的ID
    private int fragmentContentId;
    //点击的按钮ID
    private int currentTab;
    private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener;
    /*
    * 构造方法、参数Fragment集合、调用者activity、按钮集合（单选按钮）
    * */
    private SendMsg sendMsg;
    //历史页面
    private int historyPageIndex = 0;
    //当前页面
    private int currentPageIndex = 0;

    public FragmentTabAdapter(FragmentActivity fragmentActivity, List<Fragment> fragments, int fragmentContentId, RadioGroup radioGroup) {
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;
        this.fragments = fragments;
        this.radioGroup = radioGroup;
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        for(int i=0;i<fragments.size();i++){
            fragmentTransaction.add(fragmentContentId, fragments.get(i));
            fragmentTransaction.hide(fragments.get(i));
        }
        fragmentTransaction.show(fragments.get(0));
        fragmentTransaction.commit();
        radioGroup.setOnCheckedChangeListener(this);
//        NovelFragment stopCarFragment = (NovelFragment) fragments.get(1);
//
//        FriendFragment payFeeFragment = (FriendFragment) fragments.get(2);
//        payFeeFragment.setMainPageshow(mainPageShowImp);
//        sendMsg = (SendMsg) payFeeFragment;

    }

    // 设置切换动画
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();

        if (index > currentTab) {
            //ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        } else {
            //ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        }
        return ft;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            if (this.radioGroup.getChildAt(i).getId() == checkedId) {
                Fragment fragment = fragments.get(i);
                FragmentTransaction ft = obtainFragmentTransaction(i);
                //getCurrentFragment().onPause();
                if (!fragment.isAdded()) {
                    ft.add(fragmentContentId, fragment);
                }
                showTab(i); // 显示目标tab

                ft.commit();

                if (null != onRgsExtraCheckedChangedListener) {
                    onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(radioGroup, checkedId, i);
                }
            }
        }


    }

    public static class OnRgsExtraCheckedChangedListener {
        public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {

        }
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }

    public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
        return onRgsExtraCheckedChangedListener;
    }

    public void setOnRgsExtraCheckedChangedListener(OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
        this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
    }

    private void showTab(int idx) {
        FragmentTransaction ft = obtainFragmentTransaction(idx);
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);

            if (idx == i) {
                ft.show(fragment);

            } else {
                ft.hide(fragment);

            }

        }
        ft.commit();
        historyPageIndex = currentPageIndex;
        currentPageIndex = idx;
        currentTab = idx; // 更新目标tab为当前tab
    }

    /**
     * 请求页面管理器
     *
     * @return
     */
    private MainPageShowImp getPageManger() {
        return mainPageShowImp;
    }

    /**
     * 页面管理器
     */
    private MainPageShowImp mainPageShowImp = new MainPageShowImp() {
        @Override
        public void show() {

        }

        @Override
        public void hind() {

        }

        @Override
        public void back() {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(historyPageIndex);
            if (radioButton != null) {
                radioButton.setChecked(true);
            }
        }
    };
}

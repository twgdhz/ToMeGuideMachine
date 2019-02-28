package com.guidemachine.base.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import com.guidemachine.ui.view.CustomDialog;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;


/**
 * fragment基础页面
 */
public abstract class BaseFragment extends Fragment {

    protected final String TAG = this.getClass().getSimpleName();
    protected ProgressDialog progressDialog;

    protected abstract int setRootViewId();

    private LayoutInflater inflater;
    protected BackHandlerImp backHandlerImp;
    private static ArrayList<BaseFragment> fragments;
    CustomDialog logDialog;

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(setRootViewId(), container, false);
        ButterKnife.bind(this, view);
        initView(view, inflater, container, savedInstanceState);
//        setStatus();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof BackHandlerImp)) {

        } else {
            this.backHandlerImp = (BackHandlerImp) getActivity();
        }
    }

    //下面是加载框
    public void showProgressDialog() {
        if (logDialog == null) {
            logDialog = new CustomDialog(getActivity(), "加载中...");
        }
        logDialog.show();
    }

    /**
     * 对话框
     */
    protected void dismissProgressDialog() {
        if (logDialog == null) {
            return;
        }
        logDialog.dismiss();
    }

    protected abstract void initView(View view, LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState);

    protected static void Add(BaseFragment baseFragment) {
        if (!fragments.contains(baseFragment)) {
            fragments.add(baseFragment);
        }
    }

    protected void FinishAll() {
        for (BaseFragment baseFragment : fragments) {

            if (baseFragment != null) {
                baseFragment.onDestroy();
            }
        }

        fragments.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    protected void logi(String msg) {
        if (msg == null) {
            Log.i(TAG, "msg = null");
        } else {
            Log.i(TAG, msg);
        }
    }

    protected void logi(String... msg) {
        Log.i(TAG, Arrays.toString(msg));
    }

    protected void loge(Throwable e) {
        Log.e(TAG, "Throwable", e);
    }

    protected void loge(String msg) {
        if (msg == null) {
            Log.e(TAG, "msg = null");
        } else {
            Log.e(TAG, msg);
        }
    }

    protected void loge(String... msg) {
        Log.e(TAG, Arrays.toString(msg));
    }

    /**
     * @param msg
     */
    protected void msg(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param msg
     */
    protected void longMsg(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    /**
     * 通知Activity当前页面位于栈顶
     */
    @Override
    public void onStart() {
        super.onStart();
        if (backHandlerImp != null) {
            backHandlerImp.setSelectedFragment(this);
        }
    }

    /**
     * 设置沉浸式状态栏
     */
    public void setStatus() {
        Window window = getActivity().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        window.setAttributes(layoutParams);
    }
}

package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.guidemachine.constant.Constants;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.FenceBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.service.view.FenceView;
import com.guidemachine.service.view.View;
import com.guidemachine.ui.activity.MapActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class FencePresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private FenceView mFenceView;
    private BaseBean<List<FenceBean>> mBaseBean;

    public FencePresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        manager = new DataManager(mContext);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void attachView(View view) {
        mFenceView = (FenceView) view;//把请求下来的实体类交给BookView来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void getFence(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.getFence(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<List<FenceBean>>>() {
                    @Override
                    public void onCompleted() {
                        if (mBaseBean.getResultStatus().getResultCode().equals(Constants.SUCCESS_CODE)) {
                            mFenceView.onSuccess(mBaseBean);
                        } else {
                            mFenceView.onError(mBaseBean.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mFenceView.onError("请求失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean<List<FenceBean>> baseBean) {
                        mBaseBean = baseBean;
                    }
                })
        );
    }
    public void getFence2(String imei) {//网络请求就开始
        JSONObject requestFenceData = new JSONObject();
        try {
            requestFenceData.put("imei", imei);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestFenceBody = RequestBody.create(MediaType.parse("application/json"), requestFenceData.toString());
        mCompositeSubscription.add(manager.getFence(requestFenceBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<List<FenceBean>>>() {
                    @Override
                    public void onCompleted() {
                        if (mBaseBean.getResultStatus().getResultCode().equals(Constants.SUCCESS_CODE)) {
                            mFenceView.onSuccess(mBaseBean);
                        } else {
                            mFenceView.onError(mBaseBean.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mFenceView.onError("请求失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean<List<FenceBean>> baseBean) {
                        mBaseBean = baseBean;
                    }
                })
        );
    }

    private void getWeather(RequestBody requestBody){
        mCompositeSubscription.add(manager.getFence(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<List<FenceBean>>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("com.guidemachine",mBaseBean.toString()+"=====请求成功=====");
//                        if (mBaseBean.getResultStatus().getResultCode().equals(Constants.SUCCESS_CODE)) {
//                            mFenceView.onSuccess(mBaseBean);
//                        } else {
//                            mFenceView.onError(mBaseBean.getResultStatus().getResultMessage().toString());
//                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mFenceView.onError("请求失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean<List<FenceBean>> baseBean) {
                        mBaseBean = baseBean;
                    }
                })
        );
    }
}

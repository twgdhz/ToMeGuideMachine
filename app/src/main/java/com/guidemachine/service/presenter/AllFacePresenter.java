package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.FaceRegisterBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.AllFaceView;
import com.guidemachine.service.view.View;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
* @author ChenLinWang
* @email 422828518@qq.com
* @create 2018/11/7 0007 9:27
* description:
*/

public class AllFacePresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private AllFaceView mAllFaceView;
    private BaseBean<List<FaceRegisterBean>> mFaceRegisterList;

    public AllFacePresenter(Context mContext) {
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
        mAllFaceView = (AllFaceView) view;//把请求下来的实体类交给BookView来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void getAllFace() {//网络请求就开始
        mCompositeSubscription.add(manager.getAllFace()//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<List<FaceRegisterBean>>>() {
                    @Override
                    public void onCompleted() {
                        if (mFaceRegisterList != null) {
                            mAllFaceView.onSuccess(mFaceRegisterList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mAllFaceView.onError("请求失败:"+e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        mFaceRegisterList = baseBean;
                    }
                })
        );
    }
}

package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;

import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.CheckGuidePhoneBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.service.view.CheckGuidePhoneView;
import com.guidemachine.service.view.View;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 验证是否是导游的电话号码
 */

public class CheckGuidePhonePresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private CheckGuidePhoneView mCheckGuidePhoneView;
    private BaseBean<CheckGuidePhoneBean> mBaseBean;

    public CheckGuidePhonePresenter(Context mContext) {
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
        mCheckGuidePhoneView = (CheckGuidePhoneView) view;//把请求下来的实体类交给View来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void checkGuidePhone(String telephone) {//网络请求就开始
        mCompositeSubscription.add(manager.checkGuidePhone(telephone)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<CheckGuidePhoneBean>>() {
                    @Override
                    public void onCompleted() {
                        //0000请求成功码
                        if (mBaseBean != null && mBaseBean.getResultStatus().getResultCode().equals("0000")) {
                            mCheckGuidePhoneView.onSuccess(mBaseBean);
                        } else {
                            mCheckGuidePhoneView.onError(mBaseBean.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mCheckGuidePhoneView.onError("请求失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean<CheckGuidePhoneBean> baseBean) {
                        mBaseBean = baseBean;
                    }
                })
        );
    }
}

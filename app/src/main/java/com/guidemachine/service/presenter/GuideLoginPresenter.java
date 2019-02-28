package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;

import com.guidemachine.constant.Constants;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.LoginBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.GuideLoginView;
import com.guidemachine.service.view.LoginView;
import com.guidemachine.service.view.View;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class GuideLoginPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private GuideLoginView mGuideLoginView;
    private BaseBean<LoginBean> mLoginBean;

    public GuideLoginPresenter(Context mContext) {
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
        mGuideLoginView = (GuideLoginView) view;//把请求下来的实体类交给BookView来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void loginGuide(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.loginGuide(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<LoginBean>>() {
                    @Override
                    public void onCompleted() {
                        if (mLoginBean.getResultStatus().getResultCode().equals(Constants.SUCCESS_CODE)){
                            mGuideLoginView.onSuccess(mLoginBean);
                        }else {
                            mGuideLoginView.onError(mLoginBean.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mGuideLoginView.onError("请求失败:"+e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean<LoginBean> loginBean) {
                        mLoginBean = loginBean;
                    }
                })
        );
    }
}

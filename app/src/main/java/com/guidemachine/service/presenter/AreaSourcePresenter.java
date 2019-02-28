package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;

import com.guidemachine.constant.Constants;
import com.guidemachine.service.entity.AreaSourceBean;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.LoginBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.AreaSourceView;
import com.guidemachine.service.view.LoginView;
import com.guidemachine.service.view.View;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class AreaSourcePresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private AreaSourceView mAreaSourceView;
    private BaseBean<AreaSourceBean> mAreaSourceBean;

    public AreaSourcePresenter(Context mContext) {
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
        mAreaSourceView = (AreaSourceView) view;//把请求下来的实体类交给BookView来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void getAreaSourceList(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.getAreaSourceList(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<AreaSourceBean>>() {
                    @Override
                    public void onCompleted() {
                        if (mAreaSourceBean.getResultStatus().getResultCode().equals(Constants.SUCCESS_CODE)){
                            mAreaSourceView.onSuccess(mAreaSourceBean);
                        }else {
                            mAreaSourceView.onError(mAreaSourceBean.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mAreaSourceView.onError("请求失败:"+e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean<AreaSourceBean> loginBean) {
                        mAreaSourceBean = loginBean;
                    }
                })
        );
    }
}

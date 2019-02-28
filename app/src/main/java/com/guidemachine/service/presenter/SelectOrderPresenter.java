package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.SelectOrderBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.SelectOrdersView;
import com.guidemachine.service.view.View;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 个人中心订单条件搜索
 */

public class SelectOrderPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private SelectOrdersView mSelectOrdersView;
    private BaseBean<SelectOrderBean> mSelectOrders;

    public SelectOrderPresenter(Context mContext) {
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
        mSelectOrdersView = (SelectOrdersView) view;//把请求下来的实体类交给View来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void selectOrders(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.selectOrders(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<SelectOrderBean>>() {
                    @Override
                    public void onCompleted() {
                        //0000请求成功码
                        if (mSelectOrders != null && mSelectOrders.getResultStatus().getResultCode().equals("0000")) {
                            mSelectOrdersView.onSuccess(mSelectOrders);
                        } else {
                            mSelectOrdersView.onError(mSelectOrders.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mSelectOrdersView.onError("请求失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean<SelectOrderBean> bean) {
                        mSelectOrders = bean;
                    }
                })
        );
    }
}

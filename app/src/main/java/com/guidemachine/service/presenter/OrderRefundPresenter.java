package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.CreateOrderRefundReserveBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.OrderRefundView;
import com.guidemachine.service.view.View;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/10/12 0010 14:01
 * description: 申请退款
 */

public class OrderRefundPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private OrderRefundView mOrderRefundView;
    private BaseBean<CreateOrderRefundReserveBean> mRefundBean;

    public OrderRefundPresenter(Context mContext) {
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
        mOrderRefundView = (OrderRefundView) view;//把请求下来的实体类交给View来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void getOrderRefundFilling(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.createOrderRefundReserve(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<CreateOrderRefundReserveBean>>() {
                    @Override
                    public void onCompleted() {
                        //0000请求成功码
                        if (mRefundBean != null && mRefundBean.getResultStatus().getResultCode().equals("0000")) {
                            mOrderRefundView.onSuccess(mRefundBean);
                        } else {
                            mOrderRefundView.onError(mRefundBean.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mOrderRefundView.onError("请求失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean<CreateOrderRefundReserveBean> bean) {
                        mRefundBean = bean;
                    }
                })
        );
    }
}

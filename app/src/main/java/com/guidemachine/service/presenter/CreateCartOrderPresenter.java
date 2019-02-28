package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.CreateOrderBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.CreateOrderView;
import com.guidemachine.service.view.View;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 购物车实体商品订单填写页提交订单，生成订单并返回支付页面数据
 */

public class CreateCartOrderPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private CreateOrderView mCreateOrderView;
    private BaseBean<CreateOrderBean> mCreateOrderBean;

    public CreateCartOrderPresenter(Context mContext) {
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
        mCreateOrderView = (CreateOrderView) view;//把请求下来的实体类交给View来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void createCartOrder(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.createCartOrder(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<CreateOrderBean>>() {
                    @Override
                    public void onCompleted() {
                        //0000请求成功码
                        if (mCreateOrderBean != null && mCreateOrderBean.getResultStatus().getResultCode().equals("0000")) {
                            mCreateOrderView.onSuccess(mCreateOrderBean);
                        } else {
                            mCreateOrderView.onError(mCreateOrderBean.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mCreateOrderView.onError("请求失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean<CreateOrderBean> bean) {
                        mCreateOrderBean = bean;
                    }
                })
        );
    }
}

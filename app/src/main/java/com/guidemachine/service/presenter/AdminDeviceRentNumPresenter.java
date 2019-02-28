package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;

import com.guidemachine.service.entity.AdminDeviceRentNumBean;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.CreateOrderRefundReserveBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.AdminDeviceRentNumView;
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
* @create 2019/1/2 0002 12:22
* description: 设备租赁次数统计
*/

public class AdminDeviceRentNumPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private AdminDeviceRentNumView mRentNumView;
    private BaseBean<AdminDeviceRentNumBean> mRentNumBean;

    public AdminDeviceRentNumPresenter(Context mContext) {
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
        mRentNumView = (AdminDeviceRentNumView) view;//把请求下来的实体类交给View来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void selectStatisticsRentNumAdmin(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.selectStatisticsRentNumAdmin(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<AdminDeviceRentNumBean>>() {
                    @Override
                    public void onCompleted() {
                        //0000请求成功码
                        if (mRentNumBean != null && mRentNumBean.getResultStatus().getResultCode().equals("0000")) {
                            mRentNumView.onSuccess(mRentNumBean);
                        } else {
                            mRentNumView.onError(mRentNumBean.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mRentNumView.onError("请求失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean<AdminDeviceRentNumBean> bean) {
                        mRentNumBean = bean;
                    }
                })
        );
    }
}

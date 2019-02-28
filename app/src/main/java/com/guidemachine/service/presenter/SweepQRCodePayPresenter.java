package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;

import com.guidemachine.constant.Constants;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.LoginBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.GuideLoginView;
import com.guidemachine.service.view.QRCodePayView;
import com.guidemachine.service.view.View;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class SweepQRCodePayPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private QRCodePayView mQRCodePayView;
    private ResponseBody mResponseBody;

    public SweepQRCodePayPresenter(Context mContext) {
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
        mQRCodePayView = (QRCodePayView) view;//把请求下来的实体类交给BookView来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void qrCodePay(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.kindPay(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        if (mResponseBody != null) {
                            mQRCodePayView.onSuccess(mResponseBody);
                        }else {
                            mQRCodePayView.onError("二维码请求失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mQRCodePayView.onError("请求失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody loginBean) {
                        mResponseBody = loginBean;
                    }
                })
        );
    }
}

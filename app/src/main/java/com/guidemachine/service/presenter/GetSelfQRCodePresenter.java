package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;

import com.guidemachine.constant.Constants;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.GetSelfCodeBean;
import com.guidemachine.service.entity.WeatherBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.GetSelfQRCodeView;
import com.guidemachine.service.view.View;
import com.guidemachine.service.view.WeatherView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
* @author ChenLinWang
* @email 422828518@qq.com
* @create 2019/1/22 0022 15:40
* description: 自提二维码
*/
public class GetSelfQRCodePresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private GetSelfQRCodeView mGetSelfQRCodeView;
    private BaseBean<GetSelfCodeBean> mGetSelfCodeBean;

    public GetSelfQRCodePresenter(Context mContext) {
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
        mGetSelfQRCodeView = (GetSelfQRCodeView) view;//把请求下来的实体类交给BookView来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void getQrCode(String orderId) {//网络请求就开始
        mCompositeSubscription.add(manager.getQrCode(orderId)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<GetSelfCodeBean>>() {
                    @Override
                    public void onCompleted() {
                        if (mGetSelfCodeBean.getResultStatus().getResultCode().equals(Constants.SUCCESS_CODE)){
                            mGetSelfQRCodeView.onSuccess(mGetSelfCodeBean);
                        }else {
                            mGetSelfQRCodeView.onError(mGetSelfCodeBean.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mGetSelfQRCodeView.onError("请求失败:"+e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean<GetSelfCodeBean> getSelfCodeBean) {
                        mGetSelfCodeBean = getSelfCodeBean;
                    }
                })
        );
    }
}

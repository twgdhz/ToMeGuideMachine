package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.KindOrderFillingBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.KindOrderFillingView;
import com.guidemachine.service.view.View;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 *修商品订单填写
 */

public class KindOrderFillingPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private KindOrderFillingView mKindOrderFillingView;
    private BaseBean<KindOrderFillingBean> mKindOrderFillingBean;

    public KindOrderFillingPresenter(Context mContext) {
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
        mKindOrderFillingView = (KindOrderFillingView) view;//把请求下来的实体类交给View来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void kindOrderFilling(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.kindOrderFilling(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<KindOrderFillingBean>>() {
                    @Override
                    public void onCompleted() {
                        //0000请求成功码
                        if (mKindOrderFillingBean != null&&mKindOrderFillingBean.getResultStatus().getResultCode().equals("0000")) {
                            mKindOrderFillingView.onSuccess(mKindOrderFillingBean);
                        }else {
                            mKindOrderFillingView.onError(mKindOrderFillingBean.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mKindOrderFillingView.onError("请求失败:"+e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean<KindOrderFillingBean> baseBean) {
                        mKindOrderFillingBean = baseBean;
                    }
                })
        );
    }
}

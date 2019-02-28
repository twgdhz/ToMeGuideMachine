package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.CartKindOrderFillingBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.CartKindOrderFillingView;
import com.guidemachine.service.view.View;

import java.util.List;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 *购物车商品订单填写
 */

public class CartKindOrderFillingPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private CartKindOrderFillingView mCartKindOrderFillingView;
    private BaseBean<List<CartKindOrderFillingBean>> mCartKindOrderFillingList;

    public CartKindOrderFillingPresenter(Context mContext) {
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
        mCartKindOrderFillingView = (CartKindOrderFillingView) view;//把请求下来的实体类交给View来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void cartKindOrderFilling(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.cartKindOrderFilling(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<List<CartKindOrderFillingBean>>>() {
                    @Override
                    public void onCompleted() {
                        //0000请求成功码
                        if (mCartKindOrderFillingList != null&&mCartKindOrderFillingList.getResultStatus().getResultCode().equals("0000")) {
                            mCartKindOrderFillingView.onSuccess(mCartKindOrderFillingList);
                        }else {
                            mCartKindOrderFillingView.onError(mCartKindOrderFillingList.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mCartKindOrderFillingView.onError("请求失败:"+e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean<List<CartKindOrderFillingBean>> baseBean) {
                        mCartKindOrderFillingList = baseBean;
                    }
                })
        );
    }
}

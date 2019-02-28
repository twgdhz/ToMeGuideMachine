package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.BuyAgainBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.GuideDetailGoodsListView;
import com.guidemachine.service.view.View;

import java.util.List;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 商品详情页‘买过该品还买过’模块商品展示(逛了又逛)
 */

public class GuideDetailGoodsListPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private GuideDetailGoodsListView mGuideDetailGoodsListView;
    private BaseBean<List<BuyAgainBean>> mGuideDetailGoodsList;

    public GuideDetailGoodsListPresenter(Context mContext) {
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
        mGuideDetailGoodsListView = (GuideDetailGoodsListView) view;//把请求下来的实体类交给View来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void getGuideDetailGoodsList(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.getGuideDetailGoodsList(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<List<BuyAgainBean>>>() {
                    @Override
                    public void onCompleted() {
                        //0000请求成功码
                        if (mGuideDetailGoodsList != null && mGuideDetailGoodsList.getResultStatus().getResultCode().equals("0000")) {
                            mGuideDetailGoodsListView.onSuccess(mGuideDetailGoodsList);
                        } else {
                            mGuideDetailGoodsListView.onError(mGuideDetailGoodsList.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mGuideDetailGoodsListView.onError("请求失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean<List<BuyAgainBean>> list) {
                        mGuideDetailGoodsList = list;
                    }
                })
        );
    }
}
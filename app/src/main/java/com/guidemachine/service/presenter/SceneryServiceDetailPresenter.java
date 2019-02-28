package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;

import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.SceneryServiceInfoBean;
import com.guidemachine.service.entity.SceneryServiceInfoDetail;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.SceneryServiceDetailView;
import com.guidemachine.service.view.SceneryServiceInfoView;
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
 * description: 景区服务商详情
 */

public class SceneryServiceDetailPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private SceneryServiceDetailView mSceneryServiceDetailView;
    private BaseBean<SceneryServiceInfoDetail> mSceneryServiceInfoDetail;

    public SceneryServiceDetailPresenter(Context mContext) {
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
        mSceneryServiceDetailView = (SceneryServiceDetailView) view;//把请求下来的实体类交给View来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void getSceneryServiceDetail(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.selectStatisticsSceneryRentNumAdmin(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<SceneryServiceInfoDetail>>() {
                    @Override
                    public void onCompleted() {
                        //0000请求成功码
                        if (mSceneryServiceInfoDetail != null && mSceneryServiceInfoDetail.getResultStatus().getResultCode().equals("0000")) {
                            mSceneryServiceDetailView.onSuccess(mSceneryServiceInfoDetail);
                        } else {
                            mSceneryServiceDetailView.onError(mSceneryServiceInfoDetail.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mSceneryServiceDetailView.onError("请求失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean<SceneryServiceInfoDetail> bean) {
                        mSceneryServiceInfoDetail = bean;
                    }
                })
        );
    }
}

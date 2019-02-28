package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;

import com.guidemachine.service.entity.AdminStatisticsBean;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.AdminStatisticsView;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.service.view.View;

import java.util.List;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
* @author ChenLinWang
* @email 422828518@qq.com
* @create 2019/1/2 0002 11:05
* description: 超级管理员统计
*/

public class SuperAdminStatisticsPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private AdminStatisticsView mAdminStatisticsView;
    private BaseBean<List<AdminStatisticsBean>> mStatisticsBeanList;

    public SuperAdminStatisticsPresenter(Context mContext) {
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
        mAdminStatisticsView = (AdminStatisticsView) view;//把请求下来的实体类交给View来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void selectStatisticsAdmin(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.selectStatisticsAdmin(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<List<AdminStatisticsBean>>>() {
                    @Override
                    public void onCompleted() {
                        //0000请求成功码
                        if (mStatisticsBeanList != null&&mStatisticsBeanList.getResultStatus().getResultCode().equals("0000")) {
                            mAdminStatisticsView.onSuccess(mStatisticsBeanList);
                        }else {
                            mAdminStatisticsView.onError(mStatisticsBeanList.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mAdminStatisticsView.onError("请求失败:"+e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        mStatisticsBeanList = baseBean;
                    }
                })
        );
    }
}

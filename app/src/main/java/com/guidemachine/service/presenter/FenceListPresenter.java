package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;

import com.guidemachine.service.entity.AdminStatisticsBean;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.FenceBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.AdminStatisticsView;
import com.guidemachine.service.view.FenceListView;
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
* @create 2019/1/16 0016 14:42
* description: 围栏列表
*/

public class FenceListPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private FenceListView mFenceListView;
    private BaseBean<List<FenceBean>> mFenceBeanList;

    public FenceListPresenter(Context mContext) {
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
        mFenceListView = (FenceListView) view;//把请求下来的实体类交给View来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void queryfencebyguideid(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.queryfencebyguideid(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<List<FenceBean>>>() {
                    @Override
                    public void onCompleted() {
                        //0000请求成功码
                        if (mFenceBeanList != null&&mFenceBeanList.getResultStatus().getResultCode().equals("0000")) {
                            mFenceListView.onSuccess(mFenceBeanList);
                        }else {
                            mFenceListView.onError(mFenceBeanList.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mFenceListView.onError("请求失败:"+e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        mFenceBeanList = baseBean;
                    }
                })
        );
    }
}

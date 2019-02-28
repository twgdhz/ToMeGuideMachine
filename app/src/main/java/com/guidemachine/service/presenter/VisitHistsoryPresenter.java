package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;

import com.guidemachine.constant.Constants;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.VisitHistoryBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.service.view.View;
import com.guidemachine.service.view.VisitHistoryView;

import java.util.List;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class VisitHistsoryPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private VisitHistoryView mVisitHistoryView;
    private BaseBean<List<VisitHistoryBean>> visitHistoryBean;

    public VisitHistsoryPresenter(Context mContext) {
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
        mVisitHistoryView = (VisitHistoryView) view;//把请求下来的实体类交给BookView来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void getVisitHistory(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.getVisitHistory(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<List<VisitHistoryBean>>>() {
                    @Override
                    public void onCompleted() {
                        if (visitHistoryBean.getResultStatus().getResultCode().equals(Constants.SUCCESS_CODE)){
                            mVisitHistoryView.onSuccess(visitHistoryBean);
                        }else {
                            mVisitHistoryView.onError(visitHistoryBean.getResultStatus().getResultMessage().toString());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mVisitHistoryView.onError("请求失败:"+e.getMessage());
                    }
                    @Override
                    public void onNext(BaseBean<List<VisitHistoryBean>> baseBean) {
                        visitHistoryBean = baseBean;
                    }
                })
        );
    }
}

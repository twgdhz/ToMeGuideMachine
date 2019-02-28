package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;

import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.FenceBean;
import com.guidemachine.service.entity.LinkManListBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.FenceListView;
import com.guidemachine.service.view.LinkListView;
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
* @create 2019/1/18 0018 10:12
* description: 查询联系人列表
*/

public class QueryLinkPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private LinkListView mLinkListView;
    private BaseBean<List<LinkManListBean>> mLinkManList;

    public QueryLinkPresenter(Context mContext) {
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
        mLinkListView = (LinkListView) view;//把请求下来的实体类交给View来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void querylinkman(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.querylinkman(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<List<LinkManListBean>>>() {
                    @Override
                    public void onCompleted() {
                        //0000请求成功码
                        if (mLinkManList != null&&mLinkManList.getResultStatus().getResultCode().equals("0000")) {
                            mLinkListView.onSuccess(mLinkManList);
                        }else {
                            mLinkListView.onError(mLinkManList.getResultStatus().getResultMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mLinkListView.onError("请求失败:"+e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        mLinkManList = baseBean;
                    }
                })
        );
    }
}

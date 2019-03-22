package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.guidemachine.constant.Constants;
import com.guidemachine.service.entity.AreaSourceBean;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.LinkManListBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.AreaSourceView;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.service.view.SosView;
import com.guidemachine.service.view.View;
import com.guidemachine.ui.activity.SOSActivity;
import com.guidemachine.ui.guide.EmergencyLinkListActivity;
import com.guidemachine.util.L;
import com.guidemachine.util.MobileInfoUtil;
import com.guidemachine.util.share.SPHelper;

import org.json.JSONException;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class SOSLogPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private SosView sosView;
    private BaseBean mBaseBean;
    private BaseBean<List<LinkManListBean>> mLinkManList;

    public SOSLogPresenter(Context mContext) {
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
        sosView = (SosView) view;//把请求下来的实体类交给BookView来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void getSOSLog(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.getSOSLog(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onCompleted() {
                        if (mBaseBean.getResultStatus().getResultCode().equals(Constants.SUCCESS_CODE)){
                            sosView.onSuccess(mBaseBean.getResultStatus().getResultMessage().toString(),"reportSos");
                        }else {
                            sosView.onError(mBaseBean.getResultStatus().getResultMessage().toString());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        sosView.onError("请求失败:"+e.getMessage());
                    }
                    @Override
                    public void onNext(BaseBean baseBean) {
                        mBaseBean = baseBean;
                    }
                })
        );
    }
    public void getSOSLog2(Double latitude,Double longitude,String address) {//网络请求就开始
        org.json.JSONObject loginRequestData = new org.json.JSONObject();
        try {
            loginRequestData.put("address", address);
            loginRequestData.put("imei", Constants.mImei);
            loginRequestData.put("lon", latitude);
            loginRequestData.put("lat", longitude);
            loginRequestData.put("sceneryId", 1);
            loginRequestData.put("createTime", System.currentTimeMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody loginRequestBody = RequestBody.create(MediaType.parse("application/json"), loginRequestData.toString());
        mCompositeSubscription.add(manager.getSOSLog(loginRequestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onCompleted() {
                        L.gi().d("SOS上报成功："+mBaseBean.toString());
                        if (mBaseBean.getResultStatus().getResultCode().equals(Constants.SUCCESS_CODE)){
                            sosView.onSuccess(mBaseBean.getResultStatus().getResultMessage().toString(),"reportSos");
                        }else {
                            sosView.onError(mBaseBean.getResultStatus().getResultMessage().toString());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        sosView.onError("请求失败:"+e.getMessage());
                    }
                    @Override
                    public void onNext(BaseBean baseBean) {
                        mBaseBean = baseBean;
                    }
                })
        );
    }

    //查询紧急联系人
    public void querylinkman(String imei) {//网络请求就开始

        org.json.JSONObject requestData = new org.json.JSONObject();

        try {
            requestData.put("imei",imei);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        mCompositeSubscription.add(manager.querylinkman(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<List<LinkManListBean>>>() {
                    @Override
                    public void onCompleted() {
                        //0000请求成功码
//                        if (mLinkManList != null&&mLinkManList.getResultStatus().getResultCode().equals("0000")) {
//                            mLinkListView.onSuccess(mLinkManList);
//                        }else {
//                            mLinkListView.onError(mLinkManList.getResultStatus().getResultMessage().toString());
//                        }
                        L.gi().d("获取紧急联系人："+mLinkManList.toString());
                        //0000请求成功码
                        try{
                            if (mLinkManList != null&&mLinkManList.getResultStatus().getResultCode().equals("0000")) {
                                sosView.onSuccess(mLinkManList.getValue().get(0).getPhone(),"getPhone");
                            }else {
                                sosView.onError(mLinkManList.getResultStatus().getResultMessage().toString());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        sosView.onError("请求失败:"+e.getMessage());
                    }
                    @Override
                    public void onNext(BaseBean baseBean) {
                        mLinkManList = baseBean;
                    }
                })
        );
    }
}

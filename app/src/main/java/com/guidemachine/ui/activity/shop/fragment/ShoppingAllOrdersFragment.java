package com.guidemachine.ui.activity.shop.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseFragment;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.SelectOrderBean;
import com.guidemachine.service.presenter.SelectOrderPresenter;
import com.guidemachine.service.view.SelectOrdersView;
import com.guidemachine.ui.activity.order.adapter.OrderAdapter;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
* @author ChenLinWang
* @email 422828518@qq.com
* @create 2018/11/21 0021 9:27
* description: 全部订单
*/
public class ShoppingAllOrdersFragment extends BaseFragment {
    Unbinder unbinder;
    SelectOrderPresenter selectOrderPresenter;
    OrderAdapter orderAdapter;
    @BindView(R.id.ex_all_orders)
    ExpandableListView exAllOrders;
    @Override
    protected int setRootViewId() {
        return R.layout.fragment_shopping_all_orders;
    }

    @Override
    protected void initView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = this.getLayoutInflater().inflate(R.layout.fragment_shopping_all_orders, null);
        exAllOrders = view.findViewById(R.id.ex_all_orders);
//        llItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IntentUtils.openActivity(getActivity(), OrderDetailActivity.class);
//            }
//        });
        selectOrderPresenter = new SelectOrderPresenter(getContext());
        selectOrderPresenter.onCreate();
        selectOrderPresenter.attachView(selectOrdersView);
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("orderSearchStatus", 1);//订单状态 (1:全部 2:待付款 3:待自提 4:已完成 5:退款/售后)
            requestData.put("pageNum", 1);
            requestData.put("pageSize", 30);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        selectOrderPresenter.selectOrders(requestBody);
        showProgressDialog();
    }


    SelectOrdersView selectOrdersView = new SelectOrdersView() {
        @Override
        public void onSuccess(final BaseBean<SelectOrderBean> bean) {
            dismissProgressDialog();
            Log.d("SelectOrders 全部", bean.toString());
            orderAdapter = new OrderAdapter(getContext(), bean);
            try {
                exAllOrders.setAdapter(orderAdapter);
                //使所有组展开
                for (int i = 0; i < orderAdapter.getGroupCount(); i++) {
                    exAllOrders.expandGroup(i);
                }
                //使组点击无效果
                exAllOrders.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v,
                                                int groupPosition, long id) {
                        return true;
                    }
                });
                exAllOrders.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        return false;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

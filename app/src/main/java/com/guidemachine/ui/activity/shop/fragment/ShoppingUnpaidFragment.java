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

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.hyphenate.chat.EMClient.TAG;

/**
* @author ChenLinWang
* @email 422828518@qq.com
* @create 2018/11/21 0021 9:26
* description: 待付款
*/
public class ShoppingUnpaidFragment extends BaseFragment {

    SelectOrderPresenter selectOrderPresenter;
    OrderAdapter orderAdapter;
    @BindView(R.id.ex_un_payed)
    ExpandableListView exUnpayed;
    Unbinder unbinder;
    @Override
    protected int setRootViewId() {
        return R.layout.fragment_shopping_unpaid;
    }

    @Override
    protected void initView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = this.getLayoutInflater().inflate(R.layout.fragment_shopping_unpaid, null);
        exUnpayed = view.findViewById(R.id.ex_un_payed);
        selectOrderPresenter = new SelectOrderPresenter(getContext());
        selectOrderPresenter.onCreate();
        selectOrderPresenter.attachView(selectOrdersView);
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("orderSearchStatus", 2);//订单状态 (1:全部 2:待付款 3:待自提 4:已完成 5:退款/售后)
            requestData.put("pageNum", 1);
            requestData.put("pageSize", 30);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        selectOrderPresenter.selectOrders(requestBody);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    SelectOrdersView selectOrdersView = new SelectOrdersView() {
        @Override
        public void onSuccess(BaseBean<SelectOrderBean> bean) {
            Log.d("SelectOrders", bean.toString());
            orderAdapter = new OrderAdapter(getContext(), bean);
            try {
                exUnpayed.setAdapter(orderAdapter);
                Log.d("待付款", orderAdapter.toString()+"   "+exUnpayed.toString());
                //使所有组展开
                for (int i = 0; i < orderAdapter.getGroupCount(); i++) {
                    exUnpayed.expandGroup(i);
                }
                //使组点击无效果
                exUnpayed.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v,
                                                int groupPosition, long id) {
                        return true;
                    }
                });
                exUnpayed.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                    IntentUtils.openActivity(getActivity(), OrderDetailActivity.class);
                        return false;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(String result) {
            ToastUtils.msg(result);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
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


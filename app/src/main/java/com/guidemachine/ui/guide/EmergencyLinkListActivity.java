package com.guidemachine.ui.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.base.adapter.BaseRecyclerAdapter;
import com.guidemachine.base.adapter.BaseViewHolder;
import com.guidemachine.base.adapter.Listener.OnRecyclerItemClickListener;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.LinkManListBean;
import com.guidemachine.service.presenter.QueryLinkPresenter;
import com.guidemachine.service.view.LinkListView;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.MobileInfoUtil;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2019/1/17 0017 17:20
 * description: 紧急联系人列表
 */
public class EmergencyLinkListActivity extends BaseActivity {

    @BindView(R.id.ry_link_list)
    RecyclerView ryLinkList;
    BaseRecyclerAdapter adapter;
    QueryLinkPresenter queryLinkPresenter;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_complete)
    TextView tvComplete;
    List<LinkManListBean> list = new ArrayList<>();

    @Override
    protected int setRootViewId() {
        return R.layout.activity_emergency_link_list;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(EmergencyLinkListActivity.this, R.color.white);
        queryLinkPresenter = new QueryLinkPresenter(EmergencyLinkListActivity.this);
        queryLinkPresenter.onCreate();
        queryLinkPresenter.attachView(linkListView);


    }

    @Override
    protected void onResume() {
        super.onResume();
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("imei", MobileInfoUtil.getIMEI(EmergencyLinkListActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        queryLinkPresenter.querylinkman(requestBody);
        showProgressDialog();
    }

    LinkListView linkListView = new LinkListView() {
        @Override
        public void onSuccess(final BaseBean<List<LinkManListBean>> mFenceBeanList) {
            dismissProgressDialog();
            if (mFenceBeanList.getValue() != null) {
                list.addAll(mFenceBeanList.getValue());
            }
            if (mFenceBeanList.getValue().size() == 3) {
                tvComplete.setVisibility(View.GONE);
            } else {
                tvComplete.setVisibility(View.VISIBLE);
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EmergencyLinkListActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            ryLinkList.setLayoutManager(linearLayoutManager);
            adapter = new BaseRecyclerAdapter(EmergencyLinkListActivity.this, mFenceBeanList.getValue(), R.layout.item_link_list) {
                @Override
                protected void convert(BaseViewHolder helper, Object item, int position) {
                    helper.setText(R.id.et_emergency_name_one, mFenceBeanList.getValue().get(position).getName());
                    helper.setText(R.id.et_emergency_phone_one, mFenceBeanList.getValue().get(position).getPhone());
                }
            };
            ryLinkList.setAdapter(adapter);
            adapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(EmergencyLinkListActivity.this, EmergencyContactActivity.class);
                    intent.putExtra("name", mFenceBeanList.getValue().get(position).getName());
                    intent.putExtra("phone", mFenceBeanList.getValue().get(position).getPhone());
                    intent.putExtra("orderNumber", mFenceBeanList.getValue().get(position).getOrderNumber() + "");
                    intent.putExtra("type", "1");
                    startActivity(intent);
                }
            });

            tvComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EmergencyLinkListActivity.this, EmergencyContactActivity.class);
                    intent.putExtra("type", "2");
                    if (list.size()==0){
                        intent.putExtra("orderNumber", "1");
                    }else  if (list.size()==1){
                        intent.putExtra("orderNumber", "2");
                    }else  if (list.size()==2){
                        intent.putExtra("orderNumber", "3");
                    }
                    startActivity(intent);
                }
            });
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}

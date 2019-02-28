package com.guidemachine.ui.superadmin.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.guidemachine.R;
import com.guidemachine.base.adapter.BaseRecyclerAdapter;
import com.guidemachine.base.adapter.BaseViewHolder;
import com.guidemachine.base.adapter.Listener.OnRecyclerItemClickListener;
import com.guidemachine.base.ui.BaseFragment;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.SceneryServiceInfoBean;
import com.guidemachine.service.presenter.SceneryServiceInfoPresenter;
import com.guidemachine.service.view.SceneryServiceInfoView;
import com.guidemachine.ui.superadmin.SuperAdminSceneDeviceDetailActivity;
import com.guidemachine.util.IntentUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/13 0013 14:09
 * description: 超级管理员模块景区服务商
 */
public class SuperAdminServiceProviderFragment extends BaseFragment {

    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rl_right)
    RelativeLayout rlRight;
    @BindView(R.id.rl_title)
    LinearLayout rlTitle;
    //    @BindView(R.id.ll_item)
//    LinearLayout llItem;
    Unbinder unbinder;
    BaseRecyclerAdapter adapter;
    SceneryServiceInfoPresenter sceneryServiceInfoPresenter;
    @BindView(R.id.ry_scene_service_list)
    RecyclerView rySceneServiceList;

    @Override
    protected int setRootViewId() {
        return R.layout.fragment_super_admin_service;
    }


    @Override
    protected void initView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        llItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                IntentUtils.openActivity(getActivity(), SuperAdminSceneDeviceDetailActivity.class);
//            }
//        });
        sceneryServiceInfoPresenter = new SceneryServiceInfoPresenter(getContext());
        sceneryServiceInfoPresenter.onCreate();
        sceneryServiceInfoPresenter.attachView(sceneryServiceInfoView);
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("sceneryName", "");
            requestData.put("pageNum", 1);
            requestData.put("pageSize", 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        sceneryServiceInfoPresenter.getSceneryServiceInfoList(requestBody);
    }

    SceneryServiceInfoView sceneryServiceInfoView = new SceneryServiceInfoView() {
        @Override
        public void onSuccess(BaseBean<SceneryServiceInfoBean> bean) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rySceneServiceList.setLayoutManager(linearLayoutManager);
            final List<SceneryServiceInfoBean.ListBean> list = bean.getValue().getList();
            adapter = new BaseRecyclerAdapter(getContext(), list, R.layout.item_scenery_service) {
                @Override
                protected void convert(BaseViewHolder helper, Object item, int position) {
                    helper.setText(R.id.tv_total_device_num, list.get(position).getTerminalTotalNum() + "");
                    helper.setText(R.id.tv_scenery_name, list.get(position).getName() + "");
                    helper.setText(R.id.tv_online_device_num, list.get(position).getOnLineTerminalTotalNum() + "");
                    helper.setText(R.id.tv_offline_device_num, list.get(position).getOffLineTerminalTotalNum() + "");
                    helper.setText(R.id.tv_linkman, list.get(position).getChargeName() + "  "+list.get(position).getChargeTelephone());
                }
            };
            rySceneServiceList.setAdapter(adapter);
            adapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    IntentUtils.openActivity(getActivity(), SuperAdminSceneDeviceDetailActivity.class,
                            "sceneryId",list.get(position).getId()+"");
                }
            });
        }

        @Override
        public void onError(String result) {

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

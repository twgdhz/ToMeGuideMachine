package com.guidemachine.ui.admin.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.base.adapter.BaseRecyclerAdapter;
import com.guidemachine.base.adapter.BaseViewHolder;
import com.guidemachine.base.ui.BaseFragment;
import com.guidemachine.service.entity.AdminTouristTeamBean;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.presenter.AdminTouristTeamPresenter;
import com.guidemachine.service.view.AdminTouristTeamView;
import com.guidemachine.ui.admin.AdminSettingActivity;
import com.guidemachine.ui.view.AdminDialDialog;
import com.guidemachine.ui.view.AdminLogOutDialog;
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
 * @create 2018/12/28 0028 9:53
 * description: 普通管理员旅游团
 */
public class TourTeamFragment extends BaseFragment implements AdminDialDialog.DialPhone {


    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;
    Unbinder unbinder;
    AdminTouristTeamPresenter adminTouristTeamPresenter;
    @BindView(R.id.tv_center_title)
    TextView tvCenterTitle;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.rl_title)
    LinearLayout rlTitle;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ry_tour_team_list)
    RecyclerView ryTourTeamList;
    BaseRecyclerAdapter adapter;
    AdminDialDialog adminDialDialog;
    String phone;

    @Override
    protected int setRootViewId() {
        return R.layout.fragment_tour_team;
    }

    @Override
    protected void initView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rlSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openActivity(getActivity(), AdminSettingActivity.class);
            }
        });
        adminTouristTeamPresenter = new AdminTouristTeamPresenter(getContext());
        adminTouristTeamPresenter.onCreate();
        adminTouristTeamPresenter.attachView(adminTouristTeamView);
        showProgressDialog();
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("name", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        adminTouristTeamPresenter.getTouristTeamScenery(requestBody);
    }

    AdminTouristTeamView adminTouristTeamView = new AdminTouristTeamView() {
        @Override
        public void onSuccess(final BaseBean<List<AdminTouristTeamBean>> mStatisticsBeanList) {
            dismissProgressDialog();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            ryTourTeamList.setLayoutManager(linearLayoutManager);

            adapter = new BaseRecyclerAdapter(getContext(), mStatisticsBeanList.getValue(), R.layout.item_admin_tour_team) {
                @Override
                protected void convert(BaseViewHolder helper, Object item, int position) {
                    final AdminTouristTeamBean bean = mStatisticsBeanList.getValue().get(position);
                    helper.setText(R.id.tv_tour_team_name, bean.getName());
                    helper.setText(R.id.tv_phone, "电话：" + bean.getGuideTelephone());
                    helper.setText(R.id.tv_account, "账号：" + bean.getAccount());
                    helper.setText(R.id.tv_total_device_num, "总设备   " + bean.getTotalNum());
                    helper.setText(R.id.tv_offline_device_num, "离线设备  " + bean.getOfflineNum());
                    helper.setText(R.id.tv_online_device_num, "在线设备  " + bean.getOnlineNum());
                    helper.getView(R.id.img_dial).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            phone = bean.getGuideTelephone();
                            adminDialDialog = new AdminDialDialog(getActivity(), "确定拨打？\n" + bean.getGuideTelephone());
                            adminDialDialog.show();
                            adminDialDialog.setDialPhone(TourTeamFragment.this);
                        }
                    });
                }
            };
            ryTourTeamList.setAdapter(adapter);

        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
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
        adminTouristTeamPresenter.onStop();
    }

    @Override
    public void dial() {
        callPhone(phone);
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

}

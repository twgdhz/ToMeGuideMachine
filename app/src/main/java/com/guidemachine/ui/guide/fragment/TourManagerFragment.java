package com.guidemachine.ui.guide.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseFragment;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.SearchJourneyBean;
import com.guidemachine.service.presenter.AddTerminalToTouristTeamPresenter;
import com.guidemachine.service.presenter.GuideSearchJourneyPresenter;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.service.view.SearchJourneyView;
import com.guidemachine.ui.activity.chat.domain.Constant;
import com.guidemachine.ui.view.CustomDialog;
import com.guidemachine.ui.view.CustomDissolveDialog;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.Logger;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.share.SPHelper;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 导游模块 游客管理
 */
public class TourManagerFragment extends BaseFragment {


    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_right)
    RelativeLayout rlRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.img_location)
    ImageView imgLocation;
    @BindView(R.id.tv_device_number)
    TextView tvDeviceNumber;
    @BindView(R.id.tv_journey_trace)
    TextView tvJourneyTrace;
    @BindView(R.id.cs_parent)
    ConstraintLayout csParent;
    @BindView(R.id.img_add)
    ImageView imgAdd;
    Unbinder unbinder;
    CustomDissolveDialog customDissolveDialog;
    //添加游客到导游团
    AddTerminalToTouristTeamPresenter addTerminalToTouristTeamPresenter;


    public TourManagerFragment() {
        // Required empty public constructor
    }


    @Override
    protected int setRootViewId() {
        return R.layout.fragment_tour_manager;
    }


    @Override
    protected void initView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 有权限，直接do anything.
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, Constant.CODE_REQUEST);
            }
        });
        rlRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customDissolveDialog == null) {
                    customDissolveDialog = new CustomDissolveDialog(getActivity(), "确定解散该团？");
                }
                customDissolveDialog.show();
            }
        });
        addTerminalToTouristTeamPresenter = new AddTerminalToTouristTeamPresenter(getContext());
        addTerminalToTouristTeamPresenter.onCreate();
        addTerminalToTouristTeamPresenter.attachView(baseView);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.CODE_REQUEST) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {//直接扫描
                    final String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(getActivity(), "解析结果:" + result, Toast.LENGTH_LONG).show();
                    Logger.d(TAG, "获取订单号:" + result);
                    JSONObject requestData = new JSONObject();
                    try {
                        requestData.put("imei", result);
                        requestData.put("sceneryId", SPHelper.getInstance(getActivity()).getSceneryId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                    addTerminalToTouristTeamPresenter.allotTerminalToTouristTeam(requestBody);

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    BaseView baseView = new BaseView() {
        @Override
        public void onSuccess(BaseBean mBaseBean) {
            ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage() + "");
        }

        @Override
        public void onError(String result) {
            ToastUtils.msg(result + "");
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        addTerminalToTouristTeamPresenter.onStop();
    }
}

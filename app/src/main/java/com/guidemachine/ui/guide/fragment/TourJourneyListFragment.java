package com.guidemachine.ui.guide.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseFragment;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.SearchJourneyBean;
import com.guidemachine.service.presenter.ClearJourneyPresenter;
import com.guidemachine.service.presenter.DeleteItemJourneyPresenter;
import com.guidemachine.service.presenter.GuideSearchJourneyPresenter;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.service.view.DeleteItemJourneyView;
import com.guidemachine.service.view.SearchJourneyView;
import com.guidemachine.ui.guide.GuideFenceListActivity;
import com.guidemachine.ui.guide.ReleaseTourJourneyActivity;
import com.guidemachine.ui.view.CustomClearJourneyDialog;
import com.guidemachine.ui.view.SilderListView;
import com.guidemachine.ui.view.SliderView;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.share.SPHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/12 0012 16:01
 * description: 行程列表
 */
public class TourJourneyListFragment extends BaseFragment implements CustomClearJourneyDialog.ClearJourney, AdapterView.OnItemClickListener {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_right)
    RelativeLayout rlRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    Unbinder unbinder;
    CustomClearJourneyDialog customClearJourneyDialog;
    @BindView(R.id.img_add)
    ImageView imgAdd;
    //清除行程接口
    ClearJourneyPresenter clearJourneyPresenter;
    //获取行程列表
    GuideSearchJourneyPresenter guideSearchJourneyPresenter;
    @BindView(R.id.list)
    SilderListView mListView;
    //删除单挑行程
    DeleteItemJourneyPresenter deleteItemJourneyPresenter;

    @Override
    protected int setRootViewId() {
        return R.layout.fragment_tour_journey_list;
    }

    private List<SearchJourneyBean> mMessageItems = new ArrayList<SearchJourneyBean>();

    @Override
    protected void initView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        guideSearchJourneyPresenter = new GuideSearchJourneyPresenter(getContext());
        guideSearchJourneyPresenter.onCreate();
        guideSearchJourneyPresenter.attachView(searchJourneyView);
        clearJourneyPresenter = new ClearJourneyPresenter(getContext());
        clearJourneyPresenter.onCreate();
        clearJourneyPresenter.attachView(baseView);
        rlRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customClearJourneyDialog == null) {
                    customClearJourneyDialog = new CustomClearJourneyDialog(getActivity(), "确定清空所有行程？");
                }
                customClearJourneyDialog.show();
                customClearJourneyDialog.setClearJourney(TourJourneyListFragment.this);
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openActivity(getContext(), ReleaseTourJourneyActivity.class);
            }
        });

        deleteItemJourneyPresenter = new DeleteItemJourneyPresenter(getContext());
        deleteItemJourneyPresenter.onCreate();
        deleteItemJourneyPresenter.attachView(deleteItemJourneyView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMessageItems.clear();
        getJourneyList();
    }

    public void getJourneyList() {
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("sceneryId", SPHelper.getInstance(getActivity()).getSceneryId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        guideSearchJourneyPresenter.searchJourney(requestBody);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    //    public class MessageItem {
//        public int iconRes;
//        public String journeyName;
//        public String journeyTurns;
//        public String journeyTime;
//    }
    SearchJourneyView searchJourneyView = new SearchJourneyView() {
        @Override
        public void onSuccess(BaseBean<List<SearchJourneyBean>> mSearchJourneyList) {
            dismissProgressDialog();
            mMessageItems.clear();
            mMessageItems.addAll(mSearchJourneyList.getValue());
            mListView.setAdapter(new SlideAdapter());
            mListView.setOnItemClickListener(TourJourneyListFragment.this);
        }

        @Override
        public void onError(String result) {

        }
    };

    private class SlideAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        SlideAdapter() {
            super();
            mInflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mMessageItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mMessageItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            SliderView slideView = (SliderView) convertView;
            if (slideView == null) {
                View itemView = mInflater.inflate(R.layout.item_journey_list, null);

                slideView = new SliderView(getContext());
                slideView.setContentView(itemView);
                holder = new ViewHolder(slideView);
                slideView.setTag(holder);
            } else {
                holder = (ViewHolder) slideView.getTag();
            }
            final SearchJourneyBean item = mMessageItems.get(position);
            slideView.shrink();
            holder.icon.setImageResource(R.mipmap.ic_item_journey);
            if (item.getTripType() == 1) {//行程类型 (1:团行程2:指定发布行程)
                holder.tvJourneyName.setText("团行程");
            } else if (item.getTripType() == 2) {
                holder.tvJourneyName.setText(item.getImei());
            }
            holder.tvJourneyTurns.setText(item.getContent());
            holder.tvJourneyTime.setText(item.getCreateTime());
//            holder.deleteHolder.setOnClickListener(GuideFenceListActivity.this);
            holder.deleteHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.holder) {
                        Log.e(TAG, "onClick v=" + v);
//                        Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
                        JSONObject requestData = new JSONObject();
                        try {
                            requestData.put("id", item.getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                        deleteItemJourneyPresenter.deleteTripBath(requestBody);
                        showProgressDialog();
                    }
                }
            });
            return slideView;
        }

    }

    DeleteItemJourneyView deleteItemJourneyView = new DeleteItemJourneyView() {
        @Override
        public void onSuccess(BaseBean mBaseBean) {
            ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage().toString());
            getJourneyList();
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };

    private static class ViewHolder {
        public ImageView icon;
        public TextView tvJourneyName;
        public TextView tvJourneyTurns;
        public TextView tvJourneyTime;
        public ViewGroup deleteHolder;

        ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.img_item_journey);
            tvJourneyName = (TextView) view.findViewById(R.id.tv_journey_name);
            tvJourneyTurns = (TextView) view.findViewById(R.id.tv_journey_turns);
            tvJourneyTime = (TextView) view.findViewById(R.id.tv_journey_time);
            deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Log.e(TAG, "onItemClick position=" + position);

    }

    @Override
    public void clear() {
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("sceneryId", SPHelper.getInstance(getContext()).getSceneryId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        clearJourneyPresenter.clearTrip(requestBody);
        showProgressDialog();
    }

    BaseView baseView = new BaseView() {//清空
        @Override
        public void onSuccess(BaseBean mBaseBean) {
            dismissProgressDialog();
            customClearJourneyDialog.dismiss();
            ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage().toString());
            mMessageItems.clear();
            mListView.setAdapter(new SlideAdapter());
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        guideSearchJourneyPresenter.onStop();
        clearJourneyPresenter.onStop();
        deleteItemJourneyPresenter.onStop();
    }
}

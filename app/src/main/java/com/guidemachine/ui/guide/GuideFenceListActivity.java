package com.guidemachine.ui.guide;

import android.content.Intent;
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
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.FenceBean;
import com.guidemachine.service.presenter.FenceListPresenter;
import com.guidemachine.service.presenter.GuideBindFencePresenter;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.service.view.FenceListView;
import com.guidemachine.ui.view.SilderListView;
import com.guidemachine.ui.view.SliderView;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.Logger;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
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
 * @create 2018/12/24 0024 10:50
 * description: 围栏列表
 */
public class GuideFenceListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.list)
    SilderListView list;
    @BindView(R.id.img_add)
    ImageView imgAdd;
    private SilderListView mListView;

    private List<MessageItem> mMessageItems = new ArrayList<MessageItem>();
    private List<FenceBean> mFenceItems = new ArrayList<FenceBean>();

    private SliderView mLastSlideViewWithStatusOn;

    //围栏列表
    FenceListPresenter fenceListPresenter;
    //绑定围栏
    GuideBindFencePresenter guideBindFencePresenter;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_guide_fence_list;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(GuideFenceListActivity.this, R.color.white);
        initView();
        mListView = (SilderListView) findViewById(R.id.list);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openActivity(GuideFenceListActivity.this, PaintFenceActivity.class);
            }
        });
        guideBindFencePresenter = new GuideBindFencePresenter(GuideFenceListActivity.this);
        guideBindFencePresenter.onCreate();
        guideBindFencePresenter.attachView(baseView);
        showProgressDialog();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMessageItems.clear();
        fenceListPresenter = new FenceListPresenter(GuideFenceListActivity.this);
        fenceListPresenter.onCreate();
        fenceListPresenter.attachView(fenceListView);
        JSONObject requestData = new JSONObject();
        try {
//         requestData.put("sceneryId ", SPHelper.getInstance(PaintFenceActivity.this).getSceneryId());
            requestData.put("sceneryId", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        fenceListPresenter.queryfencebyguideid(requestBody);
    }

    FenceListView fenceListView = new FenceListView() {
        @Override
        public void onSuccess(BaseBean<List<FenceBean>> mFenceBeanList) {
            dismissProgressDialog();
            mFenceItems.addAll(mFenceBeanList.getValue());
            for (int i = 0; i < mFenceBeanList.getValue().size(); i++) {
                MessageItem item = new MessageItem();
                item.iconRes = R.mipmap.ic_item_fence;
                item.msg = mFenceBeanList.getValue().get(i).getName();
                item.time = "查看";
                item.id = mFenceBeanList.getValue().get(i).getId();
                item.scenerySpotName = mFenceBeanList.getValue().get(i).getScenerySpotName();
                item.type = mFenceBeanList.getValue().get(i).getType();
                item.hintMode = mFenceBeanList.getValue().get(i).getHintMode();
                item.inHintWord = mFenceBeanList.getValue().get(i).getInHintWord();
                item.outHintWord = mFenceBeanList.getValue().get(i).getOutHintWord();
                item.coordinate = mFenceBeanList.getValue().get(i).getCoordinate();
                mMessageItems.add(item);
            }
            mListView.setAdapter(new SlideAdapter());
            mListView.setOnItemClickListener(GuideFenceListActivity.this);
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };

    private void initView() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

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
                View itemView = mInflater.inflate(R.layout.list_item, null);
                slideView = new SliderView(GuideFenceListActivity.this);
                slideView.setContentView(itemView);
                holder = new ViewHolder(slideView);
                slideView.setTag(holder);
            } else {
                holder = (ViewHolder) slideView.getTag();
            }
            MessageItem item = mMessageItems.get(position);
            slideView.shrink();
            holder.icon.setImageResource(item.iconRes);
            holder.msg.setText(item.msg);
            holder.time.setText(item.time);
            holder.time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(GuideFenceListActivity.this, position + "", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GuideFenceListActivity.this, CheckFenceDetailActivity.class);
                    intent.putExtra("fence", mFenceItems.get(position));
                    startActivity(intent);
                }
            });
//            holder.deleteHolder.setOnClickListener(GuideFenceListActivity.this);
//            holder.deleteHolder.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (v.getId() == R.id.holder) {
//                        Log.e(TAG, "onClick v=" + v);
//                        Toast.makeText(GuideFenceListActivity.this, position + "", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            });
            return slideView;
        }

    }

    public class MessageItem implements Serializable {
        public int iconRes;
        public String msg;
        public String time;
        public int id;
        public String scenerySpotName;
        public String name;
        public int type;
        public int enabled;
        public int hintMode;
        public String inHintWord;
        public String outHintWord;
        public String coordinate;

    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView title;
        public TextView msg;
        public TextView time;
        public TextView tvBind;
        public ViewGroup deleteHolder;
        public RelativeLayout rlItem;

        ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.icon);
            msg = (TextView) view.findViewById(R.id.msg);
            time = (TextView) view.findViewById(R.id.time);
            tvBind = (TextView) view.findViewById(R.id.tv_bind);
            deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
            rlItem = view.findViewById(R.id.rl_item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Log.e(TAG, "onItemClick position=" + position);
        JSONObject requestData = new JSONObject();
        try {
//         requestData.put("sceneryId ", SPHelper.getInstance(PaintFenceActivity.this).getSceneryId());
            requestData.put("sceneryId", "1");
            requestData.put("fenceId", mMessageItems.get(position).id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        guideBindFencePresenter.updatefencebyguide(requestBody);
        showProgressDialog();
    }

    BaseView baseView = new BaseView() {
        @Override
        public void onSuccess(BaseBean mBaseBean) {
            dismissProgressDialog();
            ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage().toString());
//            Logger.d("FenceBean 绑定成功→"+mBaseBean.getValue().toString());
            finish();
            IntentUtils.openActivity(GuideFenceListActivity.this, SelectedFenceActivity.class);
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };

    //    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.holder) {
//            Log.e(TAG, "onClick v=" + v);
////			Toast.makeText(this, v + "", Toast.LENGTH_SHORT).show();
//
//        }
//    }
    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fenceListPresenter.onStop();
    }
}

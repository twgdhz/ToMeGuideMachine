package com.guidemachine.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guidemachine.R;
import com.guidemachine.base.adapter.BaseRecyclerAdapter;
import com.guidemachine.base.adapter.BaseViewHolder;
import com.guidemachine.base.adapter.Listener.OnRecyclerItemClickListener;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.ui.adapter.ScenePhotoAlbumAdapter;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.Logger;
import com.guidemachine.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/11/12 0012 16:49
 * description: 景区图册
 */
public class SceneAlbumActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.ry_photo)
    RecyclerView ryPhoto;
    List<String> data = new ArrayList<>();
    BaseRecyclerAdapter adapter;
    String imageUrl;
    @Override
    protected int setRootViewId() {
        return R.layout.activity_scene_album;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(SceneAlbumActivity.this, R.color.text_color4);
         imageUrl = getIntent().getExtras().getString("imageUrl");
        String name = getIntent().getExtras().getString("title");
        tvTitleCenter.setText(name);
        String[] line = imageUrl.split(",");
        for (String s : line) {
            data.add(s);
            Logger.d("相册", s.toString());
        }

        //使用瀑布流布局,第一个参数 spanCount 列数,第二个参数 orentation 排列方向
//        StaggeredGridLayoutManager recyclerViewLayoutManager =
//                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //线性布局Manager
//        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);
//        recyclerViewLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //网络布局Manager
        GridLayoutManager recyclerViewLayoutManager = new GridLayoutManager(this, 2);
        ryPhoto.setLayoutManager(recyclerViewLayoutManager);
//        data.add("http://p8p681w48.bkt.clouddn.com/pig.jpg");
//        data.add("http://pcpw0wxxu.bkt.clouddn.com/icon_20180830152819");
//        data.add("http://pcpw0wxxu.bkt.clouddn.com/icon_20180810105308");
//        for (int i = 0; i < list.getValue().getImageUrls().size(); i++) {
//            data.add(list.getValue().getImageUrls().get(i));
//        }
        ryPhoto.setLayoutManager(recyclerViewLayoutManager);
//        ScenePhotoAlbumAdapter adapter = new ScenePhotoAlbumAdapter(data, SceneAlbumActivity.this);
        //设置adapter
        adapter = new BaseRecyclerAdapter(SceneAlbumActivity.this, data, R.layout.item_scene_photo_album) {
            @Override
            protected void convert(BaseViewHolder helper, Object item, int position) {
                Glide.with(mContext).load(data.get(position)).into((ImageView) helper.getView(R.id.img_photo));
            }
        };
        ryPhoto.setAdapter(adapter);
        adapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IntentUtils.openActivity(SceneAlbumActivity.this, ShowBigImageActivity.class,"imageUrl",imageUrl);

            }
        });
    }

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

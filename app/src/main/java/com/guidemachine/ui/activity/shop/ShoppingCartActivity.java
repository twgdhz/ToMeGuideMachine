package com.guidemachine.ui.activity.shop;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.ShoppingCartListBean;
import com.guidemachine.service.presenter.DeleteCartPresenter;
import com.guidemachine.service.presenter.GetShoppingCartListPresenter;
import com.guidemachine.service.presenter.UpdateCartKindGoodsItemNumPresenter;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.service.view.DeleteCartView;
import com.guidemachine.service.view.GetShoppingCartListView;
import com.guidemachine.ui.activity.ShopDetailActivity;
import com.guidemachine.ui.activity.shop.adapter.ShoppingCarAdapter;
import com.guidemachine.ui.view.RoundCornerDialog;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
/**
* @author ChenLinWang
* @email 422828518@qq.com
* @create 2019/1/23 0023 16:45
* description: 购物车列表
*/
public class ShoppingCartActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.elv_shopping_car)
    ExpandableListView elvShoppingCar;
    @BindView(R.id.iv_select_all)
    ImageView ivSelectAll;
    @BindView(R.id.ll_select_all)
    LinearLayout llSelectAll;
    @BindView(R.id.btn_order)
    Button btnOrder;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.rl_total_price)
    RelativeLayout rlTotalPrice;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.iv_no_contant)
    ImageView ivNoContant;
    @BindView(R.id.rl_no_contant)
    RelativeLayout rlNoContant;
    @BindView(R.id.tv_titlebar_right)
    TextView tvTitlebarRight;
    private ShoppingCarAdapter shoppingCarAdapter;
    private List<ShoppingCartListBean.RecordsBean> datas;
    private List<ShoppingCartListBean.RecordsBean> noDeleteDatas;
    //获取购物车列表
    GetShoppingCartListPresenter getShoppingCartListPresenter;
    //修改购物车商品数量
    UpdateCartKindGoodsItemNumPresenter updateCartKindGoodsItemNumPresenter;
    private int flag;
    private int goodsNum;
    private String goodsPriceId;
    private String goodsId;
    private String goodsSkuId;
    //删除购物车
    DeleteCartPresenter deleteCartPresenter;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        Add(this);
        StatusBarUtils.setWindowStatusBarColor(ShoppingCartActivity.this, R.color.text_color4);
        tvTitlebarRight.setOnClickListener(this);
        getCartList();
        initExpandableListView();

    }

    public void getCartList() {//获取购物车列表
        getShoppingCartListPresenter = new GetShoppingCartListPresenter(ShoppingCartActivity.this);
        getShoppingCartListPresenter.onCreate();
        getShoppingCartListPresenter.attachView(getShoppingCartListView);
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("page", 1);
            requestData.put("pageSize", 150);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        getShoppingCartListPresenter.getShoppingCartList(requestBody);
        showProgressDialog();
    }


    GetShoppingCartListView getShoppingCartListView = new GetShoppingCartListView() {
        @Override
        public void onSuccess(BaseBean<ShoppingCartListBean> mShoppingCartListBean) {
            dismissProgressDialog();
            Log.d("ShoppingCartListBean", mShoppingCartListBean.toString());
            datas = mShoppingCartListBean.getValue().getRecords();
            noDeleteDatas = mShoppingCartListBean.getValue().getRecords();
            initExpandableListViewData(datas);

        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };
    //创建字符串 存储选中的商品id
    StringBuilder sb = new StringBuilder();

    /**
     * 初始化ExpandableListView
     * 创建数据适配器adapter，并进行初始化操作
     */
    private void initExpandableListView() {
        shoppingCarAdapter = new ShoppingCarAdapter(ShoppingCartActivity.this, llSelectAll, ivSelectAll, btnOrder, btnDelete, rlTotalPrice, tvTotalPrice);
        elvShoppingCar.setAdapter(shoppingCarAdapter);

        //删除的回调
        shoppingCarAdapter.setOnDeleteListener(new ShoppingCarAdapter.OnDeleteListener() {
            @Override
            public void onDelete(Set<String> goodsItemId) {
                Log.d("goodsItemId", goodsItemId.toString());
                for (String string : goodsItemId) {
                    sb.append(string + ",");
                }
                initDelete();
                /**
                 * 实际开发中，在此请求删除接口，删除成功后，
                 * 通过initExpandableListViewData（）方法刷新购物车数据。
                 * 注：通过bean类中的DatasBean的isSelect_shop属性，判断店铺是否被选中；
                 *                  GoodsBean的isSelect属性，判断商品是否被选中，
                 *                  （true为选中，false为未选中）
                 */

            }
        });

        //修改商品数量的回调
        shoppingCarAdapter.setOnChangeCountListener(new ShoppingCarAdapter.OnChangeCountListener() {
            @Override
            public void onChangeCount(ShoppingCartListBean.RecordsBean.KindGoodsItemsBean goods_id) {
                /**
                 * 实际开发中，在此请求修改商品数量的接口，商品数量修改成功后，
                 * 通过initExpandableListViewData（）方法刷新购物车数据。
                 */
                updateCartKindGoodsItemNumPresenter = new UpdateCartKindGoodsItemNumPresenter(ShoppingCartActivity.this);
                updateCartKindGoodsItemNumPresenter.onCreate();
                updateCartKindGoodsItemNumPresenter.attachView(baseView);
                JSONObject requestData = new JSONObject();
                try {
                    requestData.put("goodsItemId", goods_id.getGoodsItemId());
                    requestData.put("goodsNum", goods_id.getGoodsNum());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                updateCartKindGoodsItemNumPresenter.updateCartKindGoodsItemNum(requestBody);
            }
        });
    }

    /**
     * 初始化ExpandableListView的数据
     * 并在数据刷新时，页面保持当前位置
     *
     * @param datas 购物车的数据
     */
    private void initExpandableListViewData(List<ShoppingCartListBean.RecordsBean> datas) {
        if (datas != null && datas.size() > 0) {
            //刷新数据时，保持当前位置
            shoppingCarAdapter.setData(datas);
            //使所有组展开
            for (int i = 0; i < shoppingCarAdapter.getGroupCount(); i++) {
                elvShoppingCar.expandGroup(i);
            }
            //使组点击无效果
            elvShoppingCar.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    return true;
                }
            });

            tvTitlebarRight.setVisibility(View.VISIBLE);
            tvTitlebarRight.setText("编辑");
            rlNoContant.setVisibility(View.GONE);
            elvShoppingCar.setVisibility(View.VISIBLE);
            rl.setVisibility(View.VISIBLE);
            rlTotalPrice.setVisibility(View.VISIBLE);
            btnOrder.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
        } else {
            tvTitlebarRight.setVisibility(View.GONE);
            rlNoContant.setVisibility(View.VISIBLE);
            elvShoppingCar.setVisibility(View.GONE);
            rl.setVisibility(View.GONE);
        }
    }

    /**
     * 判断是否要弹出删除的dialog
     * 通过bean类中的DatasBean的isSelect_shop属性，判断店铺是否被选中；
     * GoodsBean的isSelect属性，判断商品是否被选中，
     */

    private void initDelete() {
        //判断是否有店铺或商品被选中
        //true为有，则需要刷新数据；反之，则不需要；
        boolean hasSelect = false;
        //创建临时的List，用于存储没有被选中的购物车数据
        List<ShoppingCartListBean.RecordsBean> datasTemp = new ArrayList<>();

        for (int i = 0; i < datas.size(); i++) {
            List<ShoppingCartListBean.RecordsBean.KindGoodsItemsBean> goods = datas.get(i).getKindGoodsItems();
            boolean isSelect_shop = datas.get(i).getIsSelect_shop();

            if (isSelect_shop) {
                hasSelect = true;
                continue;
            } else {
                datasTemp.add(datas.get(i));
                datasTemp.get(datasTemp.size() - 1).setKindGoodsItems(new ArrayList<ShoppingCartListBean.RecordsBean.KindGoodsItemsBean>());
            }

            for (int y = 0; y < goods.size(); y++) {
                ShoppingCartListBean.RecordsBean.KindGoodsItemsBean goodsBean = goods.get(y);
                boolean isSelect = goodsBean.getIsSelect();
                Log.d("isSelect", goodsBean.getIsSelect() + "");
                if (isSelect) {
                    hasSelect = true;
                } else {
                    datasTemp.get(datasTemp.size() - 1).getKindGoodsItems().add(goodsBean);
                }
            }
        }

        if (hasSelect) {
            showDeleteDialog(datasTemp);
        } else {
            ToastUtils.msg("请选择要删除的商品");
        }
    }

    /**
     * 展示删除的dialog（可以自定义弹窗，不用删除即可）
     *
     * @param datasTemp
     */
    private void showDeleteDialog(final List<ShoppingCartListBean.RecordsBean> datasTemp) {
        View view = View.inflate(ShoppingCartActivity.this, R.layout.dialog_two_btn, null);
        final RoundCornerDialog roundCornerDialog = new RoundCornerDialog(ShoppingCartActivity.this, 0, 0, view, R.style.RoundCornerDialog);
        roundCornerDialog.show();
        roundCornerDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        roundCornerDialog.setOnKeyListener(keylistener);//设置点击返回键Dialog不消失

        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
        TextView tv_logout_confirm = (TextView) view.findViewById(R.id.tv_logout_confirm);
        TextView tv_logout_cancel = (TextView) view.findViewById(R.id.tv_logout_cancel);
        tv_message.setText("确定要删除商品吗？");
        //取消
        tv_logout_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundCornerDialog.dismiss();
//                initExpandableListViewData(noDeleteDatas);//如果删除取消，此时的数据为datas，即原来的数据，未发生任何改变
                getCartList();
            }
        });
        //确定
        tv_logout_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundCornerDialog.dismiss();
                datas = datasTemp;//如果删除了，此时的数据为datasTemp
                initExpandableListViewData(datas);
                deleteCart(sb.toString());
                showProgressDialog();
                Log.d("goodsItemId", sb.toString());
            }
        });

    }

    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_right://编辑
                String edit = tvTitlebarRight.getText().toString().trim();
                if (edit.equals("编辑")) {
                    tvTitlebarRight.setText("完成");
                    rlTotalPrice.setVisibility(View.GONE);
                    btnOrder.setVisibility(View.GONE);
                    btnDelete.setVisibility(View.VISIBLE);
                } else {
                    tvTitlebarRight.setText("编辑");
                    rlTotalPrice.setVisibility(View.VISIBLE);
                    btnOrder.setVisibility(View.VISIBLE);
                    btnDelete.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }


    BaseView baseView = new BaseView() {
        @Override
        public void onSuccess(BaseBean mBaseBean) {
            ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage().toString());
        }

        @Override
        public void onError(String result) {
            ToastUtils.msg(result);
        }
    };

    public void deleteCart(String goodsItemIds) {
        deleteCartPresenter = new DeleteCartPresenter(ShoppingCartActivity.this);
        deleteCartPresenter.onCreate();
        deleteCartPresenter.attachView(deleteCartView);
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("goodsItemIds", goodsItemIds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        deleteCartPresenter.deleteCart(requestBody);
    }

    DeleteCartView deleteCartView = new DeleteCartView() {
        @Override
        public void onSuccess(BaseBean mBaseBean) {
            dismissProgressDialog();
            sb = new StringBuilder();
            ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage().toString());
            getCartList();

        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };
}


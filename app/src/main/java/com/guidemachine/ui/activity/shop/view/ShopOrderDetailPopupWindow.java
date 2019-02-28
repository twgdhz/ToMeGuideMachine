package com.guidemachine.ui.activity.shop.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.KindOrderFillingBean;
import com.guidemachine.ui.view.CustomDialog;

import java.math.BigDecimal;


/**
 * 购物查看价格详情
 */
public class ShopOrderDetailPopupWindow extends PopupWindow {
    Window window;
    WindowManager.LayoutParams lp;
    BaseBean<KindOrderFillingBean> kindOrderFillingBean;
    private TextView tvUnitPrice, tvNum, tvTotalPrice, tvFreightPrice, tvUnitDiscountPrice, tvDiscountNum;
    private View dividerFreight, dividerDiscount;
    private LinearLayout llFreight, llDiscount;
    CustomDialog customDialog;
    public ShopOrderDetailPopupWindow(Activity context) {
        super(context);
        init(context);
    }

    public ShopOrderDetailPopupWindow(Activity context, BaseBean<KindOrderFillingBean> mKindOrderFillingBean) {
        super(context);
        this.kindOrderFillingBean = mKindOrderFillingBean;
        init(context);
    }

    public void init(Activity context) {
        View view = View.inflate(context, R.layout.pop_shop_order_price_detail, null);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setContentView(view);
        this.setAnimationStyle(R.style.anim_menu_bottombar);
        tvUnitPrice = view.findViewById(R.id.tv_unit_price);
        tvNum = view.findViewById(R.id.tv_num);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        tvFreightPrice = view.findViewById(R.id.tv_freight_price);
        tvUnitDiscountPrice = view.findViewById(R.id.tv_unit_discount_price);
        tvDiscountNum = view.findViewById(R.id.tv_discount_num);
        dividerFreight = view.findViewById(R.id.divider_freight);
        dividerDiscount = view.findViewById(R.id.divider_discount);
        llFreight = view.findViewById(R.id.ll_freight);
        llDiscount = view.findViewById(R.id.ll_discount);
        if (customDialog == null) {
            customDialog = new CustomDialog(context, "加载中...");
        }

        if (kindOrderFillingBean.getValue() != null) {
            tvUnitPrice.setText("￥" + kindOrderFillingBean.getValue().getUnitPrice() + "");
            tvNum.setText("X" + kindOrderFillingBean.getValue().getGoodsNum());
            if (kindOrderFillingBean.getValue().getIsDiscountsPrice() == 0) {//是否有优惠 (0:无优惠1:有优惠)
                llDiscount.setVisibility(View.GONE);
                dividerDiscount.setVisibility(View.GONE);
            } else {
                llDiscount.setVisibility(View.VISIBLE);
                dividerDiscount.setVisibility(View.VISIBLE);
                tvUnitDiscountPrice.setText("-￥" + kindOrderFillingBean.getValue().getDiscountsPrice() + "");
                tvDiscountNum.setText("X" + kindOrderFillingBean.getValue().getGoodsNum());
            }

            } else {
                dividerFreight.setVisibility(View.GONE);
                llFreight.setVisibility(View.GONE);//自提时无运费
            }
//            double totalPrice = (kindOrderFillingBean.getValue().getUnitPrice() * kindOrderFillingBean.getValue().getGoodsNum())
//                    - kindOrderFillingBean.getValue().getDiscountsPrice() * kindOrderFillingBean.getValue().getGoodsNum();
        //减法
        BigDecimal bigDecimalSubtract = kindOrderFillingBean.getValue().getUnitPrice().subtract(kindOrderFillingBean.getValue().getDiscountsPrice());
        //乘法
        BigDecimal bigDecimalMultiply = bigDecimalSubtract.multiply(kindOrderFillingBean.getValue().getGoodsNum());
        double totalPrice = bigDecimalMultiply.doubleValue();
            tvTotalPrice.setText(totalPrice + "元");
        }

        //背景虚化
//        window = context.getWindow();
//        lp = window.getAttributes();
//        lp.alpha = 0.3f;
//        window.setAttributes(lp);

    /**
     * 显示popwindow
     */
    public void showPopupWindow(View view) {
        if (!isShowing()) {
//            MyPopUpWindow popupWindow=new MyPopUpWindow();
//            popupWindow.showAsDropDown(view);
//            this.showAsDropDown(view);
            this.showAtLocation(view, Gravity.BOTTOM, 0, 90);
        }

    }

    /**
     * 显示popwindow
     */
    public void dissmiss() {
        if (isShowing()) {
            dismiss();
//            lp.alpha = 1.0f;
//            window.setAttributes(lp);
        }

    }
}

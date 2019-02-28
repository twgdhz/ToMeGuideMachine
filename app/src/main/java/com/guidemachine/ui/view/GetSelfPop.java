package com.guidemachine.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.guidemachine.R;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.GetSelfCodeBean;
import com.guidemachine.service.presenter.GetSelfQRCodePresenter;
import com.guidemachine.service.view.GetSelfQRCodeView;
import com.guidemachine.util.Logger;
import com.guidemachine.util.ToastUtils;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2019/1/22 0022 13:50
 * description: 自提二维码弹框
 */
public class GetSelfPop extends PopupWindow {

    private Context context;
    CustomDialog logDialog;
    GetSelfQRCodePresenter getSelfQRCodePresenter;
    String orderId;
    ImageView imageView;

    public GetSelfPop(Context context, String orderId) {
        super(context);
        this.context = context;
        this.orderId = orderId;

        init(context);
    }

    public GetSelfPop(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GetSelfPop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(final Context context) {
        View view = View.inflate(context, R.layout.pop_get_self, null);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setContentView(view);
        imageView = view.findViewById(R.id.img_qr_code);
        ImageView imgClose = view.findViewById(R.id.img_close);
        getSelfQRCodePresenter = new GetSelfQRCodePresenter(context);
        getSelfQRCodePresenter.onCreate();
        getSelfQRCodePresenter.attachView(getSelfQRCodeView);
        getSelfQRCodePresenter.getQrCode(orderId);
        if (logDialog == null) {
            logDialog = new CustomDialog(context, "加载中...");
        }
        showProgressDialog();
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    /**
     * 显示popwindow
     */
    public void showPopupWindow(View view) {
        if (!isShowing()) {
//            MyPopUpWindow popupWindow=new MyPopUpWindow();
//            popupWindow.showAsDropDown(view);
//            this.showAsDropDown(view);
            this.showAtLocation(view, Gravity.CENTER, 0, 0);
        }

    }

    //下面是加载框
    public void showProgressDialog() {
        logDialog.show();
    }

    /**
     * 对话框
     */
    protected void dismissProgressDialog() {
        if (logDialog == null) {
            return;
        }
        logDialog.dismiss();
    }

    GetSelfQRCodeView getSelfQRCodeView = new GetSelfQRCodeView() {
        @Override
        public void onSuccess(BaseBean<GetSelfCodeBean> mGetCodeBean) {
            dismissProgressDialog();
            Logger.d("mGetCodeBean",mGetCodeBean.toString());
//            ToastUtils.msg(mGetCodeBean.getResultStatus().getResultMessage().toString());
//            Glide.with(context).load("http://tomepicture.zhihuiquanyu.com/QrCode_1547135447787.png").into(imageView);
            Glide.with(context).load(mGetCodeBean.getValue().getQrCodeUrl()).into(imageView);
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };
}

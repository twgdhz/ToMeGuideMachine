package com.guidemachine.ui.activity.shop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.CreateOrderBean;
import com.guidemachine.service.presenter.CreateGoodsOrderPresenter;
import com.guidemachine.service.presenter.SweepQRCodePayPresenter;
import com.guidemachine.service.view.CreateOrderView;
import com.guidemachine.service.view.QRCodePayView;
import com.guidemachine.util.Logger;
import com.guidemachine.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/6 0006 16:14
 * description: 立即购买支付页面
 */
public class GoodsOrderPaymentActivity extends BaseActivity {
    CreateGoodsOrderPresenter createGoodsOrderPresenter;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.tv_sale_num)
    TextView tvSaleNum;
    @BindView(R.id.rl_add_link_man)
    RelativeLayout rlAddLinkMan;
    @BindView(R.id.tv_remain_pay_time)
    TextView tvRemainPayTime;
    @BindView(R.id.ll_item_link_man)
    LinearLayout llItemLinkMan;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_real_pay)
    TextView tvRealPay;
    @BindView(R.id.ll_see_price_detail)
    LinearLayout llSeePriceDetail;
    @BindView(R.id.tv_ensure_order)
    TextView tvEnsureOrder;
    @BindView(R.id.img_qrcode)
    ImageView imgQrcode;
    private int flag;
    private String goodsId;
    private int goodsNum;
    private double price;
    private int selfType;
    private String goodsSkuId;
    private String addressId;
    private String goodsPriceId;
    private String source;
    private String goodsOrderId;
    private long closeTime;
    Date date;
    String orderNo;
    private Bitmap img;
    CreateOrderBean createOrderBean;
    //测试Url
    private String url = "http://192.168.0.102:8080/order/order/kindPay";
    SweepQRCodePayPresenter sweepQRCodePayPresenter;
    @Override
    protected int setRootViewId() {
        return R.layout.activity_goods_order_payment;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        tvTitleCenter.setText("订单支付");

        source = getIntent().getExtras().getString("source");//此字段用来判断是从订单页面过来还是直接购买。1：直接购买 2：订单页面
        createGoodsOrderPresenter = new CreateGoodsOrderPresenter(GoodsOrderPaymentActivity.this);
        createGoodsOrderPresenter.onCreate();
        createGoodsOrderPresenter.attachView(createOrderView);

        sweepQRCodePayPresenter = new SweepQRCodePayPresenter(GoodsOrderPaymentActivity.this);
        sweepQRCodePayPresenter.onCreate();
        sweepQRCodePayPresenter.attachView(qrCodePayView);
        if (source.equals("1")){
            flag = getIntent().getExtras().getInt("flag");//是否有规格
            goodsId = getIntent().getExtras().getString("goodsId");
            goodsSkuId = getIntent().getExtras().getString("goodsSkuId");
            goodsNum = getIntent().getExtras().getInt("goodsNum");
            price = getIntent().getExtras().getDouble("saleAmount");
            goodsPriceId = getIntent().getExtras().getString("goodsPriceId");
            goodsOrderId = getIntent().getExtras().getString("goodsOrderId");


            JSONObject requestData = new JSONObject();
            try {
                requestData.put("goodsId", goodsId);
                requestData.put("flag", flag);
                requestData.put("goodsSkuId", goodsSkuId);
                requestData.put("goodsPriceId", goodsPriceId);
                requestData.put("goodsNum", goodsNum);
                requestData.put("price", price);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
            createGoodsOrderPresenter.createGoodsOrder(requestBody);
            showProgressDialog();
        }else {
            llSeePriceDetail.setVisibility(View.GONE);
            goodsOrderId = getIntent().getExtras().getString("goodsOrderId");
            JSONObject loginRequestData = new JSONObject();
            try {
                loginRequestData.put("orderId", goodsOrderId);
                loginRequestData.put("type", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody loginRequestBody = RequestBody.create(MediaType.parse("application/json"), loginRequestData.toString());
            sweepQRCodePayPresenter.qrCodePay(loginRequestBody);
            showProgressDialog();
        }

    }

    CreateOrderView createOrderView = new CreateOrderView() {
        @Override
        public void onSuccess(BaseBean<CreateOrderBean> bean) {
            createOrderBean = bean.getValue();

//            ToastUtils.msg(bean.getResultStatus().getResultMessage().toString());
            closeTime = bean.getValue().getCloseTime() - System.currentTimeMillis();
            Log.d("CreateOrderBean", bean.toString());
            Log.d("CreateOrderBean", bean.getValue().getCloseTime() + "");
            Log.d("CreateOrderBean", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(bean.getValue().getCloseTime())) + "");
            Log.d("CreateOrderBean", System.currentTimeMillis() + "");
            Log.d("CreateOrderBean", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "");
            Log.d("CreateOrderBean", closeTime + "");
            Log.d("CreateOrderBean", new SimpleDateFormat("mm:ss").format(new Date(bean.getValue().getCloseTime() - System.currentTimeMillis())) + "");
            orderNo = bean.getValue().getOrderNo();
            tvSaleNum.setText("￥" + bean.getValue().getPrice() + "");
            tvRealPay.setText(bean.getValue().getPrice() + "");
            countDownTimer.start();
            JSONObject loginRequestData = new JSONObject();
            try {
                loginRequestData.put("orderNo", createOrderBean.getOrderNo());
                loginRequestData.put("type", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody loginRequestBody = RequestBody.create(MediaType.parse("application/json"), loginRequestData.toString());
            sweepQRCodePayPresenter.qrCodePay(loginRequestBody);
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };
    private CountDownTimer countDownTimer = new CountDownTimer(1797508, 1000) {//第一个参数表示总时间，第二个参数表示间隔时间。

        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("tag", "millisUntilFinished:  " + millisUntilFinished);
            date = new Date(millisUntilFinished);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
            String time = simpleDateFormat.format(date);
            Log.e("tag", "当前时间:  " + time);
            tvRemainPayTime.setText("00:" + time);
        }

        @Override
        public void onFinish() {
            Log.e("tag", "结束");
            tvRemainPayTime.setText("00:00:00");
        }
    };
    QRCodePayView qrCodePayView = new QRCodePayView() {
        @Override
        public void onSuccess(ResponseBody mResponseBody) {
            dismissProgressDialog();
            countDownTimer.start();
            Logger.d("ResponseBody",mResponseBody.toString());
            byte[] bys = new byte[0];
            try {
                bys = mResponseBody.bytes(); //注意：把byte[]转换为bitmap时，也是耗时操作，也必须在子线程
                img = BitmapFactory.decodeByteArray(bys, 0, bys.length);
                runOnUiThread(new Runnable() {//开启主线程更新UI
                    @Override
                    public void run() {
                        imgQrcode.setImageBitmap(img);

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

//    /**
//     * 异步从服务器加载图片数据
//     */
//    private void downloadImg() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Bitmap img = getImg();
//                Message msg = imgHandler.obtainMessage();
//                msg.what = 0;
//                msg.obj = img;
//                imgHandler.sendMessage(msg);
//            }
//        }).start();
//    }

//    /**
//     * 异步线程请求到的图片数据，利用Handler，在主线程中显示
//     */
//    class ImageHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//
//            switch (msg.what) {
//                case 0:
//                    img = (Bitmap) msg.obj;
//                    if (img != null) {
//                        imgQrcode.setImageBitmap(img);
//                    }
//                    break;
//
//                default:
//                    break;
//            }
//        }
//    }

    /**
     * 从服务器读取图片流数据，并转换为Bitmap格式
     *
     * @return Bitmap
     */
//    private Bitmap getImg() {
////        Bitmap img = null;
//
////        try {
////            URL imgUrl = new URL(url);
////            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
////
////            conn.setRequestMethod("POST");
////            conn.setConnectTimeout(1000 * 6);
////            conn.setDoInput(true);
////            conn.setDoOutput(true);
////            conn.setUseCaches(false);
////
////            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
////            conn.setRequestProperty("Connection", "Keep-Alive");
////            conn.setRequestProperty("Charset", "UTF-8");
////            conn.setRequestProperty("Authorization", SPHelper.getInstance(GoodsOrderPaymentActivity.this).getToken());
//////            byte[] writebytes = converJavaBeanToJson(param).getBytes();
//////            // 设置文件长度
//////            conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
////            conn.connect();
////
////            //输出流写参数
//////            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
////            String param = getParam().toString();
//////            dos.writeBytes(converJavaBeanToJson(param));
////
////
////            OutputStream outwritestream = conn.getOutputStream();
////            String par= "{\"orderNo\":\""+createOrderBean.getOrderNo()+"\",\"type\":\""+1+"\"}";
////            Logger.d("二维码",par);
////            byte[] bytes = JSON.toJSONBytes(par);
////            outwritestream.write(bytes);
////            outwritestream.flush();
////            outwritestream.close();
//////            dos.flush();
//////            dos.close();
////            final int resultCode = conn.getResponseCode();
////
////            if (HttpURLConnection.HTTP_OK == resultCode) {
////                InputStream is = conn.getInputStream();
////                img = BitmapFactory.decodeStream(is);
////                is.close();
////            } else {
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        ToastUtils.msg("异常:" + resultCode);
////                    }
////                });
////            }
////        } catch (final IOException e) {
////            e.printStackTrace();
////
////            runOnUiThread(new Runnable() {
////                @Override
////                public void run() {
////                    ToastUtils.msg(e.getMessage());
////                }
////            });
////        }
//
//        return img;
//    }

    /**
     * 测试参数
     *
     * @return
     */
//    private JSONObject getParam() {
//        JSONObject jsObj = new JSONObject();
//        try {
////            jsObj.put("orderId", createOrderBean.getOrderId());
//            jsObj.put("orderNo", createOrderBean.getOrderNo());
//            jsObj.put("type", "1");//  1:微信 2：支付宝
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
////        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsObj.toString());
//        return jsObj;
//    }


    //将javabean转换成JSON字符串
//    public static String converJavaBeanToJson(Object obj) {
//        Gson gson = null;
//        if (obj == null) {
//            return "";
//        }
//        if (gson == null) {
//            gson = new Gson();
//        }
//        String beanstr = gson.toJson(obj);
//        if (!TextUtils.isEmpty(beanstr)) {
//            return beanstr;
//        }
//        return "";
//    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

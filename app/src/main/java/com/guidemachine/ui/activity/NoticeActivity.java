package com.guidemachine.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.idl.face.platform.utils.MD5Utils;
import com.google.gson.Gson;
import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.TcpLoginBean;
import com.guidemachine.service.entity.TripBean;
import com.guidemachine.util.Logger;
import com.guidemachine.util.MobileInfoUtil;
import com.guidemachine.util.share.SPHelper;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class NoticeActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    OutputStream outputStream;
    BufferedWriter out;
    Socket s;
    @BindView(R.id.tv_notice_notice)
    TextView tvNoticeNotice;
    @BindView(R.id.tv_time1)
    TextView tvTime1;
    @BindView(R.id.tv_weather_content)
    TextView tvWeatherContent;
    @BindView(R.id.tv_time2)
    TextView tvTime2;
    @BindView(R.id.tv_notice_content)
    TextView tvNoticeContent;
    @BindView(R.id.tv_time3)
    TextView tvTime3;

    BufferedReader br;
    String temperature;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_notice;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        if (getIntent().getExtras() != null) {
            temperature = getIntent().getExtras().getString("temperature");
            tvWeatherContent.setText(temperature);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    s = new Socket("192.168.0.157", 6789);//本地
                    s = new Socket("47.92.243.217", 6789);
                    // 判断客户端和服务器是否连接成功
                    s.isConnected();
                    if (s.isConnected() == true) {
                        // final InputStream in = s.getInputStream();
                        //out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//                    long lastModified=0L;
//                    File file = new File(readAssetsTxt(NoticeActivity.this,"content"));
                        // 步骤1：从Socket 获得输出流对象OutputStream
                        // 该对象作用：发送数据
                        outputStream = s.getOutputStream();

                        // 步骤2：写入需要发送的数据到输出流对象中
                        JSONObject jsonData = JSONObject.parseObject(readAssetsTxt(NoticeActivity.this, "content"));

                        //按照字典顺序排序
                        ArrayList<String> list = new ArrayList<>();
                        String keyStr = "";
                        for (String key : jsonData.keySet()) {
                            list.add(key);
                        }
                        Collections.sort(list);
                        for (int i = 0; i < list.size(); i++) {
                            keyStr += list.get(i) + jsonData.get(list.get(i));
                        }
                        keyStr += "zhqy";
                        String ckeckSign = MD5Utils.encode(keyStr);
/*---------------------------------------------------------------------------------------*/
                        org.json.JSONObject requestData = new org.json.JSONObject();
                        requestData.put("serviceName", "login");
                        requestData.put("sign", ckeckSign);
                        org.json.JSONObject data = new org.json.JSONObject();
                        data.put("imei", MobileInfoUtil.getIMEI(NoticeActivity.this));
                        data.put("password", "865704036584648123456");
                        requestData.put("data", data);
                        Logger.d("服务端接受：" + keyStr);
                        Logger.d("服务端接受：" + ckeckSign);
                        // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞

                        // 步骤3：发送数据到服务端
                        outputStream.flush();

                        // 步骤1：创建输入流对象InputStream
                        InputStream in = s.getInputStream();

                        // 步骤2：创建输入流读取器对象 并传入输入流对象
                        outputStream.write((requestData + "\n").getBytes("utf-8"));
                        // 该对象作用：获取服务器返回的数据
                        InputStreamReader isr = new InputStreamReader(in);
                        BufferedReader br = new BufferedReader(isr);

                        // 步骤3：通过输入流读取器对象 接收服务器发送过来的数据
                        br.readLine();
                        Logger.d("服务端接受：" + br.readLine());
//                        int count=0;
//                        while (true){
//                            byte[] data=new byte[1024];
//                            int len;
//                            while((len=in.read(data)) != -1){
//                                System.out.print("客户端接受到消息>>>>");
//                                String dataStr = new String(data,0,len);
//                                System.out.println(dataStr);
//                                Logger.d("服务端解析成功02：" + dataStr);
//                                Gson gson = new Gson();
//                                TripBean tripBean = gson.fromJson(dataStr, TripBean.class);
//                                SPHelper.getInstance(NoticeActivity.this).setNoticeContent(tripBean.getMessage());
//                            }
//                        }
//                        Logger.d("服务端接受：" + br.readLine());
//                        String data= br.readLine();
//                        Logger.d("服务端接受：" + data);
//                        while(br.readLine() != null) {
//                            Logger.d("服务端解析成功01：" + br.readLine());
//                            if (!br.readLine().equals("")&&!br.readLine().equals("0x12")) {
//                                Logger.d("服务端解析成功02：" + br.readLine());
//                                Gson gson = new Gson();
//                                TripBean tripBean = gson.fromJson(br.readLine(), TripBean.class);
//                                SPHelper.getInstance(NoticeActivity.this).setNoticeContent(tripBean.getMessage());
//                            }
//                        }
                    } else {

                    }
                } catch (Exception e) {

                }
            }
        }).start();

        //定时发送心跳
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                //System.out.println("定时任务");
                try {
//                    out.write("0x11");
//                    out.flush();
                    final InputStream in = s.getInputStream();
                    out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                    // 步骤2：写入需要发送的数据到输出流对象中
                    outputStream.write(("0x11" + "\n").getBytes("utf-8"));
                    // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞

                    // 步骤3：发送数据到服务端
                    outputStream.flush();
                    // 步骤1：创建输入流对象InputStream
                    //  InputStream is = s.getInputStream();

                    // 步骤2：创建输入流读取器对象 并传入输入流对象
                    // 该对象作用：获取服务器返回的数据
                    // InputStreamReader isr = new InputStreamReader(is);
                    // BufferedReader br = new BufferedReader(isr);

                    // 步骤3：通过输入流读取器对象 接收服务器发送过来的数据
                    // br.readLine();
                    int count = 0;
                    while (true) {
                        byte[] data = new byte[1024];
                        int len;
                        while ((len = in.read(data)) != -1) {
                            System.out.print("客户端接受到消息>>>>");
                            String dataStr = new String(data, 0, len);
                            System.out.println(dataStr);
                            Logger.d("服务端解析成功02：" + dataStr);
                            if (!dataStr.equals("0x12")) {
                                Gson gson = new Gson();
                                TripBean tripBean = gson.fromJson(dataStr, TripBean.class);
                                SPHelper.getInstance(NoticeActivity.this).setNoticeContent(tripBean.getContent());
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (SPHelper.getInstance(NoticeActivity.this).getNoticeContent() != null) {
                                        tvNoticeContent.setText(SPHelper.getInstance(NoticeActivity.this).getNoticeContent());
                                    }
                                }
                            });
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 1000, 3000);

        if (SPHelper.getInstance(NoticeActivity.this).getNoticeContent() != null) {
            tvNoticeContent.setText(SPHelper.getInstance(NoticeActivity.this).getNoticeContent());
        }
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        tvTime1.setText(df.format(new Date()));
        tvTime2.setText(df.format(new Date()));
        tvTime3.setText(df.format(new Date()));
    }

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

    /**
     * 读取assets下的txt文件，返回utf-8 String
     *
     * @param context
     * @param fileName 不包括后缀
     * @return
     */
    public static String readAssetsTxt(Context context, String fileName) {
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = context.getAssets().open(fileName + ".txt");
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            // Finally stick the string into the text view.
            return text;
        } catch (IOException e) {
            // Should never happen!
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return "读取错误，请检查文件名";
    }
}

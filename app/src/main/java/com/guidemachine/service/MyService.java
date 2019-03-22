package com.guidemachine.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.ArrayMap;

import com.baidu.idl.face.platform.utils.MD5Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.guidemachine.constant.Constants;
import com.guidemachine.service.entity.TripBean;
import com.guidemachine.ui.activity.NoticeActivity;
import com.guidemachine.util.L;
import com.guidemachine.util.Logger;
import com.guidemachine.util.MobileInfoUtil;
import com.guidemachine.util.share.SPHelper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    public static final String TAG = "MyService";
    private Socket s;
    OutputStream outputStream;
    BufferedWriter out;

    //创建服务时调用
    @Override
    public void onCreate() {
        super.onCreate();
        L.gi().d(TAG, "onCreate=========启动service");
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
//                        JSONObject jsonData = JSONObject.parseObject(readAssetsTxt(NoticeActivity.this, "content"));

                        //按照字典顺序排序
//                        ArrayList<String> list = new ArrayList<>();
                        String keyStr = "";
//                        for (String key : jsonData.keySet()) {
//                            list.add(key);
//                        }
//                        Collections.sort(list);
//                        for (int i = 0; i < list.size(); i++) {
//                            keyStr += list.get(i) + jsonData.get(list.get(i));
//                        }
//                        keyStr += "zhqy";
//                        String ckeckSign = MD5Utils.encode(keyStr);

                        ArrayList<String> list = new ArrayList<>();
                        list.add("imei");
                        list.add("password");
                        Collections.sort(list);
                        for (int i = 0; i < list.size(); i++) {
                            keyStr += list.get(i) + Constants.mImei;
                        }
                        keyStr += "zhqy";
                        String ckeckSign = MD5Utils.encode(keyStr);
                        /*---------------------------------------------------------------------------------------*/
                        org.json.JSONObject requestData = new org.json.JSONObject();
                        requestData.put("serviceName", "login");
                        requestData.put("sign", ckeckSign);
                        org.json.JSONObject data = new org.json.JSONObject();
                        data.put("imei", Constants.mImei);
                        data.put("password", Constants.mImei);
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

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //服务执行的操作
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.gi().d("onStartCommand");

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
                            if (!dataStr.trim().equals("0x12")) {
                                Gson gson = new Gson();
                                TripBean tripBean = gson.fromJson(dataStr, TripBean.class);
                                SPHelper.getInstance(getApplication()).setNoticeContent(tripBean.getContent());
                            }
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (SPHelper.getInstance(NoticeActivity.this).getNoticeContent() != null) {
//                                        tvNoticeContent.setText(SPHelper.getInstance(NoticeActivity.this).getNoticeContent());
//                                    }
//                                }
//                            });
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 1000, 3000);
        return super.onStartCommand(intent, flags, startId);
    }

    //销毁服务时调用
    @Override
    public void onDestroy() {
        super.onDestroy();
        L.gi().d("onDestroy=========结束service");
    }
}

package com.guidemachine.util.serialPort;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.guidemachine.constant.Constants;
import com.guidemachine.util.L;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import android_serialport_api.SerialPort;

/**
 * Created by WangChaowei on 2017/12/7.
 */

public class SerialPortUtils {

    private final String TAG = "SerialPortUtils";
    private String path = "/dev/ttyMT1";
    private int baudrate = 115200;
    //    private String path = "/dev/ttyS0";
//    private int baudrate = 9600;
    public boolean serialPortStatus = false; //是否打开串口标志
    public String data_;
    public boolean threadStatus; //线程状态，为了安全终止线程

    public SerialPort serialPort = null;
    public InputStream inputStream = null;
    public OutputStream outputStream = null;
    public ChangeTool changeTool = new ChangeTool();

    private static SerialPortUtils INSTANCE;
    public SerialPortUtils() {
    }
    /** 获取本类单例 */
    public static SerialPortUtils getInstance() {
        if (INSTANCE == null) {
            synchronized (SerialPortUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SerialPortUtils();
                }
            }
        }
        return INSTANCE;
    }
    /**
     * 打开串口
     *
     * @return serialPort串口对象
     */
    @SuppressLint("LongLogTag")
    public SerialPort openSerialPort() {
        try {
            serialPort = new SerialPort(new File(path), baudrate, 0, 8, 1);
            this.serialPortStatus = true;
            threadStatus = false; //线程状态
            //获取打开的串口中的输入输出流，以便于串口数据的收发
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();

            new ReadThread().start(); //开始线程监控是否有数据要接收
        } catch (Exception e) {
            Log.e(TAG, "openSerialPort: 打开串口异常：" + e.toString());
            return serialPort;
        }
        Log.d(TAG, "openSerialPort: 打开串口");
        return serialPort;
    }
    public boolean isConnect(){
        return serialPortStatus;
    }
    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        try {
            if(inputStream!=null){
                inputStream.close();
            }
            if(outputStream!=null){
                outputStream.close();
            }
            this.serialPortStatus = false;
            this.threadStatus = true; //线程状态
            serialPort.close();
        } catch (IOException e) {
            Log.e(TAG, "closeSerialPort: 关闭串口异常：" + e.toString());
            return;
        }
        Log.d(TAG, "closeSerialPort: 关闭串口成功");
    }

    /**
     * 发送串口指令（字符串）
     */
    @SuppressLint("LongLogTag")
    public void sendSerialPort(Message message) {
        Log.d(TAG, "sendSerialPort: 发送数据");

        try {
//            byte[] sendData = Integer.toHexString(data).getBytes();
//            byte[] sendData = data.getBytes(); //string转byte[]
//            sendData = HexCommandtoByte(data.getBytes());
            byte[] sendData = new byte[]{0x36, 0x02, 0x77, 0x02};
//            byte[] sendData = new byte[]{0x36,0x02,0x77,0x03,0x7A};
            this.data_ = new String(sendData); //byte[]转string
            if (sendData.length > 0) {
//                outputStream.write(hexToBytes(data));
//                outputStream.write(sendData);
                sendData = assembleCommand(message);
                this.data_ =byteArrayToHexString(sendData); //byte[]转string
                outputStream.write(sendData);
                outputStream.write('\n');
                //outputStream.write('\r'+'\n');
                outputStream.flush();
                Log.d("com.dfxh.wang.serialport_test", "sendSerialPort: 串口数据发送成功" + sendData + "  转换的数据：" + Arrays.toString(sendData));
            }
        } catch (Exception e) {
            Log.e(TAG, "sendSerialPort: 串口数据发送失败：" + e.toString());
        }

    }

    /**
     * 发送数据hex
     * @param sHex
     */
    public void sendHex(String sHex){
        L.gi().d("===========发送命令========："+sHex);
        byte[] bOutArray = MyFunc.HexToByteArr(sHex);
        send(bOutArray);
    }
    public void send(byte[] bOutArray){
        try
        {
            outputStream.write(bOutArray);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 单开一线程，来读数据
     */
    private class ReadThread extends Thread {
        @SuppressLint("LongLogTag")
        @Override
        public void run() {
            super.run();
            //判断进程是否在运行，更安全的结束进程
            while (!threadStatus) {
                Log.d("run", "进入线程接受数据run");
//                Toast.makeText(mContext,"接收到数据：",Toast.LENGTH_SHORT).show();
                //64   1024
                byte[] buffer = new byte[32];
                int size; //读取数据的大小
                try {
//                    Log.d(TAG, "run: 接收到了数据size：" + Arrays.toString(buffer));
                    size = inputStream.read(buffer);

                    if (size > 0) {
                        String hex = changeTool.ByteArrToHex(buffer).trim();//63-12-05-40-1234567890ABCDEF1234567890123456-90
                        String type = hex.substring(4,6);
                        String code = hex.substring(8,40);//63 12 05 35 1234567890ABCDEF123456789012345685
                        String signal = hex.substring(6,8);
                        switch (type){
                            case "05"://上报
                                if(Integer.parseInt(signal)<42){
                                    L.gi().d("开始上报RFID========="+changeTool.ByteArrToHex(buffer));
                                    onDataReceiveListener.onDataReceive(code,signal);
                                }
                                break;
                        }
                        L.gi().d("run: 接收到模块返回数据：" + changeTool.ByteArrToHex(buffer));
                    }
                } catch (Exception e) {
//                    Log.e(TAG, "run: 数据读取异常：" +e.toString());
                }
            }
        }
    }

    public OnDataReceiveListener onDataReceiveListener = null;

//    public static interface OnDataReceiveListener {
//        public void onDataReceive(byte[] buffer, int size);
//    }
    public static interface OnDataReceiveListener {
        public void onDataReceive(String data,String signal);
    }
    public void setOnDataReceiveListener(OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }

    public static byte[] assembleCommand(Message message) {
        try {
            byte[] data = message.getData();
            int dataLength = data == null ? 0 : data.length;
//            int length = 8;

//            if (dataLength > 0)
//                length = 0 + dataLength;
//            Log.d("命令长度",length+"=======");

            ByteBuffer buffer = ByteBuffer.allocate(5);
            // 以下为帧前置数据 ======================================================================
            buffer.put(Constants.VISE_COMMAND_HEAD_FLAG_36);//帧头
            buffer.put((byte) dataLength);//帧长
            buffer.put(message.getCommandType()); // 命令类型
            buffer.put(message.getCommandData()); // 功能码

            buffer.put((byte) (message.getCommandType()+message.getCommandData()));
            byte[] bytes = buffer.array();
//            Log.d("com.dfxh.wang.serialport_test转换的数据","== bytes:" + Arrays.toString(bytes) + " == 指令工厂组装指令：" + byteArrayToHexString(bytes));
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHexResult(Message message){

        byte[] data = message.getData();
        int dataLength = data == null ? 0 : data.length;
        ByteBuffer buffer = ByteBuffer.allocate(5);
        // 以下为帧前置数据 ======================================================================
        buffer.put(Constants.VISE_COMMAND_HEAD_FLAG_36);//帧头
        buffer.put((byte) dataLength);//帧长
        buffer.put(message.getCommandType()); // 命令类型
        buffer.put(message.getCommandData()); // 功能码
        buffer.put((byte) (message.getCommandType()+message.getCommandData()));
        String res = bytesToHexString(buffer.array());
        return  res;
    }

    public static String byteArrayToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return "";
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}

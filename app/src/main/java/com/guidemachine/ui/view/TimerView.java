package com.guidemachine.ui.view;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/11/13 0013 14:33
 * description: 验证码按钮计时器
 */
public class TimerView {
    private TextView view;
    private int count = 0;
    private int timeCount = 60;
    Timer timer;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public TimerView(TextView view) {
        this.view = view;
    }

    public int getTimeCount() {
        return timeCount;
    }

    public void setTimeCount(int timeCount) {
        this.timeCount = timeCount;
    }

    public View getView() {
        return view;
    }

    public void setView(Button view) {
        this.view = view;
    }

    Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    view.setEnabled(false);
                    view.setTextColor(Color.parseColor("#999999"));
                    view.setText("重新发送(" + count + "s)");
                    Log.d("time_log", "" + count);
                    if (count < 0) {
                        view.setEnabled(true);
                        timer.cancel();
                        view.setText("重新发送");
                        view.setTextColor(Color.parseColor("#999999"));
                    }
                }
            }
        }
    };

    public void start() {
        Log.d("TimeView is ", "Start");
        timer = new Timer();
        count = timeCount;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                count--;
                Message message = new Message();
                message.what = 1;
                timeHandler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 0, 1000);

    }
}

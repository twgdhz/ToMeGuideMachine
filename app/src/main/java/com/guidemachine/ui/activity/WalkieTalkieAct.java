package com.guidemachine.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.constant.Constants;
import com.guidemachine.ui.activity.chat.widget.MyChronometer;
import com.guidemachine.ui.view.RippleView;
import com.guidemachine.util.L;
import com.guidemachine.util.serialPort.Message;
import com.guidemachine.util.serialPort.SerialPortUtils;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 对讲机界面
 */
public class WalkieTalkieAct extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.title_id)
    RelativeLayout mTitleLayout;
    @BindView(R.id.speech_image)
    ImageView mSpeechImage;
    @BindView(R.id.time_text)
    TextView mTimeText;

    private int mFlag = 1;//1 未讲话 2讲话中
    TextView mTitleText;
    private long baseTimer;

    @Override
    protected void InitialView() {
        mTitleText = mTitleLayout.findViewById(R.id.title_text);
        mTitleText.setText("对讲机");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mSpeechImage.setOnClickListener(this);
        if (!SerialPortUtils.getInstance().isConnect()) {
            SerialPortUtils.getInstance().openSerialPort();
        }
        if(SerialPortUtils.getInstance().isConnect()){
            //设置频道
            Message message = new Message();
            message.setCommandType(Constants.VISE_COMMAND_HEAD_FLAG_02);
            message.setCommandData(Constants.VISE_COMMAND_HEAD_FLAG_01);
            message.setData(new byte[]{message.getCommandType(), message.getCommandData()});
            SerialPortUtils.getInstance().sendHex(SerialPortUtils.getInstance().getHexResult(message));
        }
    }

    @Override
    protected int setRootViewId() {
        return R.layout.walkie_talkie_layout;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @OnClick(R.id.back_layout)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.speech_image:
                Message message;
                switch (mFlag) {
                    case 1://开启对讲机
                        if(SerialPortUtils.getInstance().isConnect()) {
                           message = new Message();
                            message.setCommandType(Constants.VISE_COMMAND_HEAD_FLAG_03);
                            message.setCommandData(Constants.VISE_COMMAND_HEAD_FLAG_01);
                            message.setData(new byte[]{message.getCommandType(), message.getCommandData()});
                            SerialPortUtils.getInstance().sendHex(SerialPortUtils.getInstance().getHexResult(message));
                            mFlag = 2;
                            mTitleText.setText("正在讲话...");
                            mSpeechImage.setImageResource(R.mipmap.speeching_image);
                            mTimeText.setVisibility(View.VISIBLE);
                            handler.sendEmptyMessage(0);
                        }
                        break;
                    case 2://关闭对讲机
                        if(SerialPortUtils.getInstance().isConnect()) {
                            baseTimer = 0;
                            message = new Message();
                            message.setCommandType(Constants.VISE_COMMAND_HEAD_FLAG_03);
                            message.setCommandData(Constants.VISE_COMMAND_HEAD_FLAG_02);
                            message.setData(new byte[]{message.getCommandType(), message.getCommandData()});
                            SerialPortUtils.getInstance().sendHex(SerialPortUtils.getInstance().getHexResult(message));
                            mFlag = 1;
                            mTitleText.setText("对讲机");
                            mSpeechImage.setImageResource(R.mipmap.no_speech_image);
                            mTimeText.setVisibility(View.GONE);
                            handler.removeMessages(0);
                        }
                        break;
                }
                break;
            case R.id.back_layout:
                finish();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (0 == baseTimer) {
                        baseTimer = SystemClock.elapsedRealtime();
                    }
                    int time = (int) ((SystemClock.elapsedRealtime() - baseTimer) / 1000);
                    String hh = new DecimalFormat("00").format(time / 3600);
                    String mm = new DecimalFormat("00").format(time % 3600 / 60);
                    String ss = new DecimalFormat("00").format(time % 60);
                    if (null != mTimeText) {
                        mTimeText.setText(hh + ":" + mm + ":" + ss);
                    }
                    handler.sendEmptyMessageDelayed(0,1000);
                    break;
            }
        }
    };
}

package com.guidemachine.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.App;
import com.bumptech.glide.Glide;
import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.util.L;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 音频播放界面
 */
public class VoiceExplainAct extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.seek_bar)
    SeekBar mSeekBar;
    @BindView(R.id.play_stop_image)
    ImageView mIsPlayImage;
    @BindView(R.id.titile_content_text)
    TextView mTitleContent;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.voice_start_time)
    TextView mCurrentTime;
    @BindView(R.id.voice_max_time)
    TextView mMaxTime;

    private int flag = 1;//1 暂停 2播放
    private MediaPlayer player;
    List<String> imageData = new ArrayList<>();
    String imageUrl;
    private String mPlayFlag = "0";

    @Override
    protected void InitialView() {
        imageUrl = getIntent().getExtras().getString("imageUrl");
        String name = getIntent().getExtras().getString("title");
        mPlayFlag = getIntent().getExtras().getString("flag");
        mTitleContent.setText(name);//标题
        String[] line = imageUrl.split(",");
        for (String s : line) {
            imageData.add(s);
            L.gi().d("相册"+s.toString());
        }

        mTitleText.setText("语音讲解");

        initPlay();
        //设置最大时间
        updateTime(mMaxTime,player.getDuration());

        mSeekBar.setMax(player.getDuration());
        mSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imageData);
        //设置轮播时间
        banner.setDelayTime(2500);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    Runnable updateThread = new Runnable() {
        public void run() {
            //获得歌曲现在播放位置并设置成播放进度条的值
            mSeekBar.setProgress(player.getCurrentPosition());
            //每次延迟100毫秒再启动线程
            App.getMyApplication().mHandler.postDelayed(updateThread, 100);

        }
    };


    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser==true){
                player.seekTo(progress);
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            L.gi().d("播放完毕");
            mIsPlayImage.setImageResource(R.mipmap.music_stop_image);
            flag = 1;
            player.stop();
            initPlay();
            mSeekBar.setProgress(0);
        }
    };

    private void initPlay(){
        if(player!=null){
            player.reset();
        }
        if (mPlayFlag.equals("46")){
            player = MediaPlayer.create(this, R.raw.voice1);
        }else if (mPlayFlag.equals("45")){
            player = MediaPlayer.create(this, R.raw.voice2);
        }if (mPlayFlag.equals("18")){
            player = MediaPlayer.create(this, R.raw.voice3);
        }if (mPlayFlag.equals("17")){
            player = MediaPlayer.create(this, R.raw.voice4);
        }if (mPlayFlag.equals("3")){
            player = MediaPlayer.create(this, R.raw.voice5);
        }
        player.setOnCompletionListener(onCompletionListener);
    }
    @Override
    protected int setRootViewId() {
        return R.layout.voice_explain_layout;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @OnClick({R.id.back_layout,R.id.play_stop_image})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.play_stop_image:
                switch (flag){
                    case 1:
                        mIsPlayImage.setImageResource(R.mipmap.music_play_image);
                        App.getMyApplication().mHandler.post(updateThread);
                        flag = 2;
                        player.start();
                        handler.sendEmptyMessage(1);
                        break;
                    case 2:
                        mIsPlayImage.setImageResource(R.mipmap.music_stop_image);
                        //取消线程
                        App.getMyApplication().mHandler.removeCallbacks(updateThread);
                        flag = 1;
                        player.pause();
                        break;
                }
                break;
        }
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    int position = player.getCurrentPosition();
                    updateTime(mCurrentTime,position);
                    handler.sendEmptyMessageDelayed(1, 500);
                    break;
            }
        }
    };
    //更新时间
    private void updateTime(TextView textView,int millisecond){
        int second = millisecond/1000;
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String str = null;
        if(hh!=0){
            str = String.format("%02d:%02d:%02d",hh,mm,ss);
        }else {
            str = String.format("%02d:%02d",mm,ss);
        }
        textView.setText(str);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getMyApplication().mHandler.removeCallbacks(updateThread);
        handler.removeMessages(1);
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
            }
    }
}

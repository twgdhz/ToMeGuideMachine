package com.guidemachine.ui.activity.video;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.guidemachine.R;
import com.guidemachine.util.StatusBarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayerActivity extends AppCompatActivity {

    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.activity_local_video)
    RelativeLayout activityLocalVideo;
    private MediaController mediaController;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);
        StatusBarUtils.setWindowStatusBarColor(VideoPlayerActivity.this, R.color.white);
        setStatus();
        id = getIntent().getExtras().getString("id");
        mediaController = new MediaController(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoView.setLayoutParams(layoutParams);
        //设置视频控制器
        videoView.setMediaController(mediaController);
        //下面android.resource://是固定的，com.guidemachine是包名，R.raw.sw是你raw文件夹下的视频文件
        if (id.equals("3")) {
            videoView.setVideoURI(Uri.parse("android.resource://com.guidemachine/" + R.raw.qingchengshan1));
        } else if (id.equals("17")) {
            videoView.setVideoURI(Uri.parse("android.resource://com.guidemachine/" + R.raw.qingchengshan2));
        } else if (id.equals("18")) {
            videoView.setVideoURI(Uri.parse("android.resource://com.guidemachine/" + R.raw.qingchengshan3));
        } else if (id.equals("45")) {
            videoView.setVideoURI(Uri.parse("android.resource://com.guidemachine/" + R.raw.qingchengshan4));
        } else if (id.equals("46")) {
            videoView.setVideoURI(Uri.parse("android.resource://com.guidemachine/" + R.raw.qingchengshan5));
        }

        //设置视频路径
//        Uri uri = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
//        videoView.setVideoURI(uri);
        videoView.start();
        //播放完成回调
        videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());
    }

    /**
     * 设置沉浸式状态栏
     */
    public void setStatus() {
        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        window.setAttributes(layoutParams);
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(VideoPlayerActivity.this, "播放完成", Toast.LENGTH_SHORT).show();
//            btnAgain.setVisibility(View.VISIBLE);
//            btnBack.setVisibility(View.VISIBLE);
            finish();
        }
    }
}

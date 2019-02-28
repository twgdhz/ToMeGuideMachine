package com.guidemachine.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;


import com.guidemachine.R;
import com.guidemachine.event.MessageEventPlayComplete;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class VoiceUtil {

    private static VoiceUtil singletonLazy;
    MediaPlayer player = new MediaPlayer();
    int flagId = 0;

    private VoiceUtil() {

    }

    public static VoiceUtil getInstance() {
        if (null == singletonLazy) {
//            try {
//                // 模拟在创建对象之前做一些准备工作
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            singletonLazy = new VoiceUtil();
        }
        return singletonLazy;
    }

    /**
     *  * 监听系统静音模式
     */

    public void modeIndicater(Context mContext, int id) {

        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        final int ringerMode = am.getRingerMode();

        switch (ringerMode) {

            case AudioManager.RINGER_MODE_NORMAL://普通模式

                playFromRawFile(mContext, id);

                break;

            case AudioManager.RINGER_MODE_VIBRATE://静音模式

                break;

            case AudioManager.RINGER_MODE_SILENT://震动模式

                break;

        }

    }

    /**
     *  * 提示音
     * <p>
     *  * @param mContext
     * <p>
     *  
     */

    public void playFromRawFile(Context mContext, int id) {

//        try {
//            AssetFileDescriptor file = null;
//            if (id == 2) {
//                file = mContext.getResources().openRawResourceFd(R.raw.voice1);
//            } else if (id == 3) {
//                file = mContext.getResources().openRawResourceFd(R.raw.voice2);
//            } else if (id == 10) {
//                file = mContext.getResources().openRawResourceFd(R.raw.voice3);
//            } else if (id == 11) {
//                file = mContext.getResources().openRawResourceFd(R.raw.voice4);
//            }
////            player.stop();
////            player.reset();
//            player.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
//            file.close();
//            if (!player.isPlaying()) {
//                player.prepare();
//                player.start();
//            } else {
//                player.stop();
//                player.reset();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (id == 2) {
//            player = MediaPlayer.create(mContext, R.raw.voice1);
//        } else if (id == 3) {
//            player = MediaPlayer.create(mContext, R.raw.voice2);
//        } else if (id == 10) {
//            player = MediaPlayer.create(mContext, R.raw.voice3);
//        } else if (id == 11) {
//            player = MediaPlayer.create(mContext, R.raw.voice4);
//        }
        try {
//            mpMediaPlayer.prepare();
//            ToastUtils.msg(id + "   " + player.isPlaying());
//            if (flagId != id) {
//                player.stop();
//                player.release();
//                flagId = id;
//            }
            if (player.isPlaying() == true) {
                player.stop();
                player.release();
                if (id == 3) {
                    player = MediaPlayer.create(mContext, R.raw.voice1);
                } else if (id == 17) {
                    player = MediaPlayer.create(mContext, R.raw.voice2);
                } else if (id == 18) {
                    player = MediaPlayer.create(mContext, R.raw.voice3);
                } else if (id == 45) {
                    player = MediaPlayer.create(mContext, R.raw.voice4);
                } else if (id == 46) {
                    player = MediaPlayer.create(mContext, R.raw.voice5);
                }
            } else {
                if (id == 3) {
                    player = MediaPlayer.create(mContext, R.raw.voice1);
                    player.start();
                } else if (id == 17) {
                    player = MediaPlayer.create(mContext, R.raw.voice2);
                    player.start();
                } else if (id == 18) {
                    player = MediaPlayer.create(mContext, R.raw.voice3);
                    player.start();
                } else if (id == 45) {
                    player = MediaPlayer.create(mContext, R.raw.voice4);
                    player.start();
                }else if (id == 46) {
                    player = MediaPlayer.create(mContext, R.raw.voice5);
                    player.start();
                }
            }
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.d("tag", "播放完毕");
                    //根据需要添加自己的代码。。。
                    EventBus.getDefault().post(new MessageEventPlayComplete(true));
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
            ToastUtils.msg("异常: "+e.getMessage());

        }
    }
}
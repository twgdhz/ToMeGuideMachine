package com.guidemachine.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.guidemachine.R;

import java.util.HashMap;

/**
 * SoundPool最大只能申请1M的内存空间，这就意味着我们只能用一些很短的声音片段，而不是用它来播放歌曲或者做游戏背景音乐
 */
public class SoundManager {
    // SoundPool对象
    public static SoundPool mSound = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

    private static SoundManager soundManager;


    public static SoundManager getInstence() {
        if (soundManager == null) {
            soundManager = new SoundManager();
        }
        return soundManager;
    }
    /**
     * 初始化音频文件
     */
    public  void initMusic(Context context){
        //分别加入到SoundPool中
        mSound.load(context, R.raw.voice1, 1);// 1
        mSound.load(context, R.raw.voice2, 1);// 2
        mSound.load(context, R.raw.voice3, 1);// 3
        mSound.load(context, R.raw.voice4, 1);// 4
        mSound.load(context, R.raw.voice5, 1);// 4
    }

    /**
     * 播放MP3资源
     * @param resId 资源ID
     */
    public void StartMusic(int resId){
        /**
         * 第一个参数为播放音频ID
         * 第二个 第三个为音量
         * 第四个为优先级
         * 第五个为是否循环播放
         * 第六个设置播放速度
         * 返回值 不为0即代表成功
         */
        int type = mSound.play(resId, 45, 45, 0, 0, 1);
    }
}

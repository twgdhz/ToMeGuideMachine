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
    public void initMusic(Context context) {
        //分别加入到SoundPool中
        try{

        mSound.load(context, R.raw.voice1, 1);// 1
        mSound.load(context, R.raw.voice2, 2);// 2
        mSound.load(context, R.raw.voice3, 3);// 3
        mSound.load(context, R.raw.voice4, 4);// 4
        mSound.load(context, R.raw.voice5, 5);// 4
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 播放MP3资源
     *
     * @param resId 资源ID
     */
    public int StartMusic(int resId) {
        /**
         * 第一个参数为播放音频ID
         * 第二个 第三个为音量
         * 第四个为优先级
         * 第五个为是否循环播放
         * 第六个设置播放速度
         * 返回值 不为0即代表成功
         */
        L.gi().d("===========播放ID==============："+resId);
        int type = 0;
        try{
        type = mSound.play(resId, 45, 45, 0, 0, 1);
        mSound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Logger.d("com.guidemachine", "onLoadComplete播放完毕");

            }
        });
        }catch (Exception e){
            e.printStackTrace();
        }
        return type;
    }

    //继续播放指定音乐
    public void resume(int resId) {
        if (mSound != null) {
            mSound.resume(resId);
        }
    }
    public void pauseMusic(int resId) {
        if (mSound != null) {
            mSound.pause(resId);
        }
    }

    public void stopMusic(int id) {
        if (mSound != null) {
            int mCurrentId = 0;
            if (id==46){
                mCurrentId = 1;
            }else if (id==45){
                mCurrentId = 2;
            }if (id==18){
                mCurrentId = 3;
            }if (id==17){
                mCurrentId = 4;
            }if (id==3){
                mCurrentId = 5;
            }
            mSound.stop(mCurrentId);
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mSound != null)
            mSound.release();
        mSound = null;
    }
}

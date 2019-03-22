package com.guidemachine.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.guidemachine.R;

public class MediaPlayerUtils {
    private static MediaPlayerUtils mediaPlayerUtils;
    private MediaPlayer player;
    public static MediaPlayerUtils getInstance () {
        if(mediaPlayerUtils==null){
            mediaPlayerUtils = new MediaPlayerUtils();
        }
        return mediaPlayerUtils;
    }
    private void startVoice(Context context,Uri uri){
        if(player!=null) player.reset();
        player = MediaPlayer.create(context, uri);
        player.start();
    }
    private void startVoiceId(Context context,int id){
        if(player!=null) player.reset();
        switch (id){
            case 1:
                player = MediaPlayer.create(context, R.raw.voice1);
                break;
            case 2:
                player = MediaPlayer.create(context, R.raw.voice2);
                break;
            case 3:
                player = MediaPlayer.create(context, R.raw.voice3);
                break;
            case 4:
                player = MediaPlayer.create(context, R.raw.voice4);
                break;
            case 5:
                player = MediaPlayer.create(context, R.raw.voice5);
                break;
        }
        player.start();
    }

    private void pauseVoice(){
        if(player!=null){
            player.pause();
        }
    }
    private void stopVoice(){
        if(player!=null){
            player.stop();
        }
    }

    private void release(){
        if(player!=null){
            player.stop();
            player.reset();
            player = null;
        }
    }
}

package com.guidemachine.event;


import com.guidemachine.service.entity.GoodSpecBean;

/**
 * Description:eventBus的事件
 */

public class MessageEventPlayComplete {


    public boolean isPlay;


    public MessageEventPlayComplete(boolean isPlay) {
        this.isPlay = isPlay;
    }
}
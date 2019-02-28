package com.uuzuche.lib_zxing.event;

/**
 * Created by csoone on 2017/2/17.
 * Description:eventBus的事件
 */

public class EventSweepMessage {

    public String id;

    public String name;

    public EventSweepMessage(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "EventSweepMessage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
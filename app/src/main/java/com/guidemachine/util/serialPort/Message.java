package com.guidemachine.util.serialPort;

import java.util.Arrays;

public class Message {
    //命令类型
    private byte commandType;

    //命令data
    private byte commandData;

    //通信数据
    private byte[] data;


    public Message() {
    }

    public Message(byte commandType, byte commandData, byte[] data) {
        this.commandType = commandType;

        this.commandData = commandData;
    }

    public byte getCommandData() {
        return commandData;
    }

    public void setCommandData(byte commandData) {
        this.commandData = commandData;
    }

    public byte getCommandType() {
        return commandType;
    }

    public void setCommandType(byte commandType) {
        this.commandType = commandType;
    }


    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "Message{" +
                "commandType=" + commandType +
                ", commandData=" + commandData +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}

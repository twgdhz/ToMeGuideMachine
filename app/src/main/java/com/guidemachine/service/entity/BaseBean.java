package com.guidemachine.service.entity;

import java.io.Serializable;

public class BaseBean<T> implements Serializable {

    /**
     * resultStatus : {"resultCode":"0000","resultMessage":null,"timeStamp":"2018-07-27 14:25:51.844"}
     * value : dG9rZW4tMzJhNTk0NWI1YjFmNDU4NTk3YWIwZWIyODM5M2IxYmM=
     * exception : null
     * attachments : {}
     */

    private ResultStatusBean resultStatus;
    private T value;
    private Object exception;
    private AttachmentsBean attachments;

    public ResultStatusBean getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatusBean resultStatus) {
        this.resultStatus = resultStatus;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Object getException() {
        return exception;
    }

    public void setException(Object exception) {
        this.exception = exception;
    }

    public AttachmentsBean getAttachments() {
        return attachments;
    }

    public void setAttachments(AttachmentsBean attachments) {
        this.attachments = attachments;
    }

    public static class ResultStatusBean implements Serializable{
        /**
         * resultCode : 0000
         * resultMessage : null
         * timeStamp : 2018-07-27 14:25:51.844
         */

        private String resultCode;
        private Object resultMessage;
        private String timeStamp;

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public Object getResultMessage() {
            return resultMessage;
        }

        public void setResultMessage(Object resultMessage) {
            this.resultMessage = resultMessage;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        @Override
        public String toString() {
            return "ResultStatusBean{" +
                    "resultCode='" + resultCode + '\'' +
                    ", resultMessage=" + resultMessage +
                    ", timeStamp='" + timeStamp + '\'' +
                    '}';
        }
    }

    public static class AttachmentsBean implements Serializable{
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "resultStatus=" + resultStatus +
                ", value='" + value + '\'' +
                ", exception=" + exception +
                ", attachments=" + attachments +
                '}';
    }
}

package com.guidemachine.service.entity;

import java.io.Serializable;
import java.util.List;

public class SceneryServiceInfoDetail implements Serializable{

    /**
     * totalRentNum : 0
     * data : [{"date":"2019-01-02","time":"10","totalNum":5,"onlineNum":0,"offlineNum":5,"rentNum":0},{"date":"2019-01-02","time":"12","totalNum":5,"onlineNum":0,"offlineNum":5,"rentNum":0},{"date":"2019-01-02","time":"13","totalNum":5,"onlineNum":0,"offlineNum":5,"rentNum":0},{"date":"2019-01-02","time":"14","totalNum":5,"onlineNum":0,"offlineNum":5,"rentNum":0},{"date":"2019-01-02","time":"15","totalNum":5,"onlineNum":0,"offlineNum":5,"rentNum":0},{"date":"2019-01-02","time":"16","totalNum":10,"onlineNum":0,"offlineNum":10,"rentNum":0},{"date":"2019-01-02","time":"17","totalNum":10,"onlineNum":0,"offlineNum":10,"rentNum":0}]
     */

    private int totalRentNum;
    private List<DataBean> data;

    public int getTotalRentNum() {
        return totalRentNum;
    }

    public void setTotalRentNum(int totalRentNum) {
        this.totalRentNum = totalRentNum;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * date : 2019-01-02
         * time : 10
         * totalNum : 5
         * onlineNum : 0
         * offlineNum : 5
         * rentNum : 0
         */

        private String date;
        private String time;
        private int totalNum;
        private int onlineNum;
        private int offlineNum;
        private int rentNum;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }

        public int getOnlineNum() {
            return onlineNum;
        }

        public void setOnlineNum(int onlineNum) {
            this.onlineNum = onlineNum;
        }

        public int getOfflineNum() {
            return offlineNum;
        }

        public void setOfflineNum(int offlineNum) {
            this.offlineNum = offlineNum;
        }

        public int getRentNum() {
            return rentNum;
        }

        public void setRentNum(int rentNum) {
            this.rentNum = rentNum;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "date='" + date + '\'' +
                    ", time='" + time + '\'' +
                    ", totalNum=" + totalNum +
                    ", onlineNum=" + onlineNum +
                    ", offlineNum=" + offlineNum +
                    ", rentNum=" + rentNum +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SceneryServiceInfoDetail{" +
                "totalRentNum=" + totalRentNum +
                ", data=" + data +
                '}';
    }
}

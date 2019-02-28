package com.guidemachine.service.entity;

import java.io.Serializable;
import java.util.List;

public class TeamLocationBean implements Serializable{

    /**
     * num : {"totalNum":9,"onlineNum":0,"offlineNum":9}
     * coordinateList : [{"id":1,"lon":103.567311,"lat":30.908131},{"id":3,"lon":103.567312,"lat":30.908132},{"id":11,"lon":103.567313,"lat":30.908133},{"id":12,"lon":103.567314,"lat":30.908134},{"id":13,"lon":103.567315,"lat":30.908138},{"id":24,"lon":0,"lat":0},{"id":51,"lon":103.567316,"lat":30.908137},{"id":52,"lon":103.567317,"lat":30.908136},{"id":53,"lon":103.567318,"lat":30.908135}]
     * terminalInfoList : [{"id":1,"imei":"956680617111247","codeMachine":"8758","telephone":"18736020283","isOnline":0,"battery":"0%","onLineTime":"2018-12-14 11:58:35","positionTime":"2018-12-29 14:00:00","positionAddress":"四川省都江堰市西南部青城山风景区"},{"id":3,"imei":"956680617111245","codeMachine":"8768","telephone":"18736020259","isOnline":0,"battery":"0%","onLineTime":"2018-12-14 11:58:35","positionTime":"2018-12-29 14:00:00","positionAddress":"四川省都江堰市西南部青城山风景区"},{"id":11,"imei":"956680617111248","codeMachine":"8765","telephone":"18736020282","isOnline":0,"battery":"0%","onLineTime":"2018-12-14 11:58:35","positionTime":"2018-12-29 14:00:00","positionAddress":"四川省都江堰市西南部青城山风景区"},{"id":12,"imei":"956680617111249","codeMachine":"8766","telephone":"18736020284","isOnline":0,"battery":"0%","onLineTime":"2018-12-14 11:58:35","positionTime":"2018-12-29 14:00:00","positionAddress":"四川省都江堰市西南部青城山风景区"},{"id":13,"imei":"956680617111250","codeMachine":"8767","telephone":"18736020271","isOnline":0,"battery":"0%","onLineTime":"2018-12-14 11:58:35","positionTime":"2018-12-29 14:00:00","positionAddress":"四川省都江堰市西南部青城山风景区"},{"id":24,"imei":"956680617111261","codeMachine":"8778","telephone":"18736020269","isOnline":0,"battery":"0%","onLineTime":"2018-12-14 11:58:35","positionTime":"2018-12-29 14:00:00","positionAddress":"四川省都江堰市西南部青城山风景区"},{"id":51,"imei":"956680617111290","codeMachine":"8805","telephone":"15282297723","isOnline":0,"battery":"0%","onLineTime":"2018-12-14 11:58:35","positionTime":"2018-12-29 14:00:00","positionAddress":"四川省都江堰市西南部青城山风景区"},{"id":52,"imei":"865704036584648","codeMachine":"545616115","telephone":"13000000000","isOnline":0,"battery":"0%","onLineTime":"2018-12-14 11:58:35","positionTime":"2018-12-29 14:00:00","positionAddress":"四川省都江堰市西南部青城山风景区"},{"id":53,"imei":"865704036584655","codeMachine":"54561615","telephone":"13000050000","isOnline":0,"battery":"0%","onLineTime":"2018-12-14 11:58:35","positionTime":"2018-12-29 14:00:00","positionAddress":"四川省都江堰市西南部青城山风景区"}]
     */

    private NumBean num;
    private List<CoordinateListBean> coordinateList;
    private List<TerminalInfoListBean> terminalInfoList;

    public NumBean getNum() {
        return num;
    }

    public void setNum(NumBean num) {
        this.num = num;
    }

    public List<CoordinateListBean> getCoordinateList() {
        return coordinateList;
    }

    public void setCoordinateList(List<CoordinateListBean> coordinateList) {
        this.coordinateList = coordinateList;
    }

    public List<TerminalInfoListBean> getTerminalInfoList() {
        return terminalInfoList;
    }

    public void setTerminalInfoList(List<TerminalInfoListBean> terminalInfoList) {
        this.terminalInfoList = terminalInfoList;
    }

    public static class NumBean {
        /**
         * totalNum : 9
         * onlineNum : 0
         * offlineNum : 9
         */

        private int totalNum;
        private int onlineNum;
        private int offlineNum;

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
    }

    public static class CoordinateListBean implements Serializable{
        /**
         * id : 1
         * lon : 103.567311
         * lat : 30.908131
         */

        private int id;
        private double lon;
        private double lat;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        @Override
        public String toString() {
            return "CoordinateListBean{" +
                    "id=" + id +
                    ", lon=" + lon +
                    ", lat=" + lat +
                    '}';
        }
    }

    public static class TerminalInfoListBean implements Serializable{
        /**
         * id : 1
         * imei : 956680617111247
         * codeMachine : 8758
         * telephone : 18736020283
         * isOnline : 0
         * battery : 0%
         * onLineTime : 2018-12-14 11:58:35
         * positionTime : 2018-12-29 14:00:00
         * positionAddress : 四川省都江堰市西南部青城山风景区
         */

        private int id;
        private String imei;
        private String codeMachine;
        private String telephone;
        private int isOnline;
        private String battery;
        private String onLineTime;
        private String positionTime;
        private String positionAddress;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getCodeMachine() {
            return codeMachine;
        }

        public void setCodeMachine(String codeMachine) {
            this.codeMachine = codeMachine;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public int getIsOnline() {
            return isOnline;
        }

        public void setIsOnline(int isOnline) {
            this.isOnline = isOnline;
        }

        public String getBattery() {
            return battery;
        }

        public void setBattery(String battery) {
            this.battery = battery;
        }

        public String getOnLineTime() {
            return onLineTime;
        }

        public void setOnLineTime(String onLineTime) {
            this.onLineTime = onLineTime;
        }

        public String getPositionTime() {
            return positionTime;
        }

        public void setPositionTime(String positionTime) {
            this.positionTime = positionTime;
        }

        public String getPositionAddress() {
            return positionAddress;
        }

        public void setPositionAddress(String positionAddress) {
            this.positionAddress = positionAddress;
        }

        @Override
        public String toString() {
            return "TerminalInfoListBean{" +
                    "id=" + id +
                    ", imei='" + imei + '\'' +
                    ", codeMachine='" + codeMachine + '\'' +
                    ", telephone='" + telephone + '\'' +
                    ", isOnline=" + isOnline +
                    ", battery='" + battery + '\'' +
                    ", onLineTime='" + onLineTime + '\'' +
                    ", positionTime='" + positionTime + '\'' +
                    ", positionAddress='" + positionAddress + '\'' +
                    '}';
        }

    }

    @Override
    public String toString() {
        return "TeamLocationBean{" +
                "num=" + num +
                ", coordinateList=" + coordinateList +
                ", terminalInfoList=" + terminalInfoList +
                '}';
    }
}

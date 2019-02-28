package com.guidemachine.service.entity;

import java.io.Serializable;

public class TcpLoginBean implements Serializable {

    /**
     * serviceName : login
     * data : {"password":"imeitest","imei":"865704036584648"}
     * sign : 75cba3eb5262bae81fe28fab3d35d6b0
     */

    private String serviceName;
    private DataBean data;
    private String sign;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public static class DataBean {
        /**
         * password : imeitest
         * imei : 865704036584648
         */

        private String password;
        private String imei;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "password='" + password + '\'' +
                    ", imei='" + imei + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TcpLoginBean{" +
                "serviceName='" + serviceName + '\'' +
                ", data=" + data +
                ", sign='" + sign + '\'' +
                '}';
    }
}

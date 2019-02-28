package com.guidemachine.service.entity;

import java.io.Serializable;

public class WeatherBean implements Serializable {

    /**
     * weather_id : {"fa":"00","fb":"00"}
     * week : 星期四
     * city : 成都
     * dressing_index : 较冷
     * travel_index : 较适宜
     * wash_index : 较适宜
     * comfort_index :
     * exercise_index : 较适宜
     * dressing_advice : 建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。
     * uv_index : 中等
     * drying_index :
     * temperature : 6℃~13℃
     * weather : 晴
     * date_y : 2018年11月22日
     * wind : 持续无风向微风
     */

    private WeatherIdBean weather_id;
    private String week;
    private String city;
    private String dressing_index;
    private String travel_index;
    private String wash_index;
    private String comfort_index;
    private String exercise_index;
    private String dressing_advice;
    private String uv_index;
    private String drying_index;
    private String temperature;
    private String weather;
    private String date_y;
    private String wind;

    public WeatherIdBean getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(WeatherIdBean weather_id) {
        this.weather_id = weather_id;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDressing_index() {
        return dressing_index;
    }

    public void setDressing_index(String dressing_index) {
        this.dressing_index = dressing_index;
    }

    public String getTravel_index() {
        return travel_index;
    }

    public void setTravel_index(String travel_index) {
        this.travel_index = travel_index;
    }

    public String getWash_index() {
        return wash_index;
    }

    public void setWash_index(String wash_index) {
        this.wash_index = wash_index;
    }

    public String getComfort_index() {
        return comfort_index;
    }

    public void setComfort_index(String comfort_index) {
        this.comfort_index = comfort_index;
    }

    public String getExercise_index() {
        return exercise_index;
    }

    public void setExercise_index(String exercise_index) {
        this.exercise_index = exercise_index;
    }

    public String getDressing_advice() {
        return dressing_advice;
    }

    public void setDressing_advice(String dressing_advice) {
        this.dressing_advice = dressing_advice;
    }

    public String getUv_index() {
        return uv_index;
    }

    public void setUv_index(String uv_index) {
        this.uv_index = uv_index;
    }

    public String getDrying_index() {
        return drying_index;
    }

    public void setDrying_index(String drying_index) {
        this.drying_index = drying_index;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getDate_y() {
        return date_y;
    }

    public void setDate_y(String date_y) {
        this.date_y = date_y;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public static class WeatherIdBean {
        /**
         * fa : 00
         * fb : 00
         */

        private String fa;
        private String fb;

        public String getFa() {
            return fa;
        }

        public void setFa(String fa) {
            this.fa = fa;
        }

        public String getFb() {
            return fb;
        }

        public void setFb(String fb) {
            this.fb = fb;
        }

        @Override
        public String toString() {
            return "WeatherIdBean{" +
                    "fa='" + fa + '\'' +
                    ", fb='" + fb + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WeatherBean{" +
                "weather_id=" + weather_id +
                ", week='" + week + '\'' +
                ", city='" + city + '\'' +
                ", dressing_index='" + dressing_index + '\'' +
                ", travel_index='" + travel_index + '\'' +
                ", wash_index='" + wash_index + '\'' +
                ", comfort_index='" + comfort_index + '\'' +
                ", exercise_index='" + exercise_index + '\'' +
                ", dressing_advice='" + dressing_advice + '\'' +
                ", uv_index='" + uv_index + '\'' +
                ", drying_index='" + drying_index + '\'' +
                ", temperature='" + temperature + '\'' +
                ", weather='" + weather + '\'' +
                ", date_y='" + date_y + '\'' +
                ", wind='" + wind + '\'' +
                '}';
    }
}

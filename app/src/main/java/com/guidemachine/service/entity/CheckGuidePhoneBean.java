package com.guidemachine.service.entity;

import java.io.Serializable;

public class CheckGuidePhoneBean implements Serializable{

    /**
     * id : 07d0dba85933422a9c4b62a476f03418
     * username : 导游30
     * nickName : null
     * password : null
     * sex : null
     * telphone : 13526410325
     * enable : 1
     * member : 6
     * createTime : null
     * createUserId : null
     * updateTime : null
     * updateUserId : null
     * birthday : null
     * provinceId : null
     * cityId : null
     * areaId : null
     * imageUrl : null
     * address : null
     */

    private String id;
    private String username;
    private Object nickName;
    private Object password;
    private Object sex;
    private String telphone;
    private int enable;
    private String member;
    private Object createTime;
    private Object createUserId;
    private Object updateTime;
    private Object updateUserId;
    private Object birthday;
    private Object provinceId;
    private Object cityId;
    private Object areaId;
    private Object imageUrl;
    private Object address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Object getNickName() {
        return nickName;
    }

    public void setNickName(Object nickName) {
        this.nickName = nickName;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public Object getSex() {
        return sex;
    }

    public void setSex(Object sex) {
        this.sex = sex;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public Object getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Object createUserId) {
        this.createUserId = createUserId;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public Object getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Object updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Object getBirthday() {
        return birthday;
    }

    public void setBirthday(Object birthday) {
        this.birthday = birthday;
    }

    public Object getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Object provinceId) {
        this.provinceId = provinceId;
    }

    public Object getCityId() {
        return cityId;
    }

    public void setCityId(Object cityId) {
        this.cityId = cityId;
    }

    public Object getAreaId() {
        return areaId;
    }

    public void setAreaId(Object areaId) {
        this.areaId = areaId;
    }

    public Object getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Object imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "CheckGuidePhoneBean{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", nickName=" + nickName +
                ", password=" + password +
                ", sex=" + sex +
                ", telphone='" + telphone + '\'' +
                ", enable=" + enable +
                ", member='" + member + '\'' +
                ", createTime=" + createTime +
                ", createUserId=" + createUserId +
                ", updateTime=" + updateTime +
                ", updateUserId=" + updateUserId +
                ", birthday=" + birthday +
                ", provinceId=" + provinceId +
                ", cityId=" + cityId +
                ", areaId=" + areaId +
                ", imageUrl=" + imageUrl +
                ", address=" + address +
                '}';
    }
}

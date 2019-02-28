package com.guidemachine.service.entity;

import java.io.Serializable;

public class EmergencyLinkManBean implements Serializable{

    /**
     * id : 3
     * name : 紧急联系人一
     * phone : 18200445526
     * orderNumber : 3
     */

    private int id;
    private String name;
    private String phone;
    private int orderNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "EmergencyLinkManBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", orderNumber=" + orderNumber +
                '}';
    }
}

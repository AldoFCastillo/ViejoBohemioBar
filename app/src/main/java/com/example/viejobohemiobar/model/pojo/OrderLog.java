package com.example.viejobohemiobar.model.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderLog implements Serializable {

    private List<Order> orderList;

    public OrderLog() {
    }

    public OrderLog(List<Order> orderList) {
        this.orderList = orderList;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}

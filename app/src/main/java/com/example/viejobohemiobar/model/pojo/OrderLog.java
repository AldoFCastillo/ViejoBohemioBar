package com.example.viejobohemiobar.model.pojo;

import java.util.ArrayList;
import java.util.List;

public class OrderLog {

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

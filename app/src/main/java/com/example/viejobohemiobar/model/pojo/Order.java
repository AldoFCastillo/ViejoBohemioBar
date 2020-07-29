package com.example.viejobohemiobar.model.pojo;

import java.io.Serializable;

public class Order implements Serializable {

    private Result result;
    private String total;
    private Boolean cash;
    private Boolean callWait;
    private String comments;
    private String id;
    private String table;
    private String time;
    private static Order order;

    public Order() {

    }

    private Order(Result result, String total, Boolean cash, Boolean callWait, String comments, String id, String table, String time) {
        this.result = result;
        this.total = total;
        this.cash = cash;
        this.callWait = callWait;
        this.comments = comments;
        this.id = id;
        this.table = table;
        this.time = time;
    }

    public static Order getOrderInstance(Result result, String total, Boolean cash, Boolean callWait, String comments, String id, String table, String time){
        if(order==null){
            order = new Order(result, total, cash,callWait, comments,id, table, time);
        }
        return order;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Boolean getCash() {
        return cash;
    }

    public void setCash(Boolean cash) {
        this.cash = cash;
    }

    public Boolean getCallWait() {
        return callWait;
    }

    public void setCallWait(Boolean callWait) {
        this.callWait = callWait;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

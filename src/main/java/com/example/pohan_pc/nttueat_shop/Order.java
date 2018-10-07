package com.example.pohan_pc.nttueat_shop;

import java.util.ArrayList;

/**
 * Created by POHAN-PC on 2017/12/23.
 */

public class Order {
    private String OrderId,OrderPerson,OrderPhone,OrderTime,OrderStatus,OrderNotiID;
    private ArrayList<food> OrderItem = new ArrayList<food>();
    private int OrderTotal;

    Order(){}

    public Order(String Id, String Person, String Phone, String Time, String Status, ArrayList<food> Item, int Total, String notiid){
        OrderId = Id;
        OrderPerson = Person;
        OrderPhone = Phone;
        OrderTime = Time;
        OrderStatus = Status;
        OrderItem = Item;
        OrderTotal = Total;
        OrderNotiID = notiid;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getOrderPerson() {
        return OrderPerson;
    }

    public void setOrderPerson(String orderPerson) {
        OrderPerson = orderPerson;
    }

    public String getOrderPhone() {
        return OrderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        OrderPhone = orderPhone;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public ArrayList<food> getOrderItem() {
        return OrderItem;
    }

    public void setOrderItem(ArrayList<food> orderItem) {
        OrderItem = orderItem;
    }

    public int getOrderTotal() {
        return OrderTotal;
    }

    public void setOrderTotal(int orderTotal) {
        OrderTotal = orderTotal;
    }

    public String getOrderNotiID() {
        return OrderNotiID;
    }

    public void setOrderNotiID(String orderNotiID) {
        OrderNotiID = orderNotiID;
    }
}

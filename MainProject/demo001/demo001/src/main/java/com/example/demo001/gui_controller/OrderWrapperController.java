package com.example.demo001.gui_controller;

import com.example.demo001.domain.OrderManagement.ProductOrder;

public class OrderWrapperController {
    public ProductOrder productOrder;
    public double orderCost;

    public OrderWrapperController(ProductOrder po, double oc) {
        this.productOrder = po;
        this.orderCost = oc;
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    public double getOrderCost() {
        return orderCost;
    }
}

package com.example.demo001.gui_controller;

import com.example.demo001.domain.Products.Product;

public class CartWrapperController {

    public Product product;
    public Integer productAmount;

    public CartWrapperController(Product p, Integer pA) {
        this.product = p;
        this.productAmount = pA;
    }

    public Product getProduct() {
        return this.product;
    }

    public Integer getProductAmount() {
        return this.productAmount;
    }
}

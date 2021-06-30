package com.example.demo001.domain.OrderManagement;

import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.Products.Product;
import lombok.Data;
import javax.persistence.*;

/**
 * Representation of ordered item
 * - determines information about ordered products, factory producing the item and to which order it belongs
 */
@Entity
@Data
public class OrderItem {

    /** Ordered item's autogenerated unique identification number */
    @Id
    @GeneratedValue
    private long id;

    /** Order in which item was ordered */
    @OneToOne
    private ProductOrder order;

    /** Ordered product */
    @OneToOne
    private Product product;

    /** Ordered amount of product */
    @Column
    private int productAmount;

    /** Factory producing ordered item */
    @OneToOne
    private Factory factory;

    public OrderItem() {}
    public OrderItem(ProductOrder order, Product product, int productAmount) {
        this.order = order;
        this.product = product;
        this.productAmount = productAmount;
        this.factory = null;
    }
}
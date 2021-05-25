package com.example.demo001.domain.OrderManagement;


import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.Products.Product;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    private Orders order;

    @OneToOne
    private Product product;

    @Column
    private int productAmount;

    @OneToOne
    private Factory factory;

}

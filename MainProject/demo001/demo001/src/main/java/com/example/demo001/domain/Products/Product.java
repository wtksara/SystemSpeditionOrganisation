package com.example.demo001.domain.Products;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue
    private Integer productId;

    @Column
    private String productName;

    @Column
    private double productPrize;

}

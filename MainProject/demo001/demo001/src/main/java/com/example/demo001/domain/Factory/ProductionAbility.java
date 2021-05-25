package com.example.demo001.domain.Factory;


import com.example.demo001.domain.Products.Product;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ProductionAbility {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    private Product myProduct;

    @Column
    private int productAmount;

    @Column
    private int reservedProducts;

}

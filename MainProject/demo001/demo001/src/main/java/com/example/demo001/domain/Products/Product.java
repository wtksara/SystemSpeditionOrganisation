package com.example.demo001.domain.Products;

import lombok.Data;
import javax.persistence.*;

/**
 * Representation of product produced by the factories
 * - describes the product.
 */
@Entity
@Data
public class Product {

    /** Product's autogenerated unique identification number */
    @Id
    @GeneratedValue
    private long productId;

    /** Name of the product */
    @Column
    private String productName;

    /** Name of the product */
    @Column
    private ProductType productType;

    /** Prize of the product */
    @Column
    private double productPrize;

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public ProductType getProductType() {
        return productType;
    }

    public double getProductPrize() {
        return productPrize;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public void setProductPrize(double productPrize) {
        this.productPrize = productPrize;
    }

    public Product() {}
    public Product(String productName, ProductType productType, double productPrize)  {
        this.productName = productName;
        this.productType = productType;
        this.productPrize = productPrize;
    }
}

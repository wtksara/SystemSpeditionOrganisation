package com.example.demo001.repository;

import com.example.demo001.domain.Products.Product;
import com.example.demo001.domain.Products.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getAllByProductType(ProductType productType);
}

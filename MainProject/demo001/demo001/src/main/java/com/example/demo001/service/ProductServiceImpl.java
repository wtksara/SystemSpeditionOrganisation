package com.example.demo001.service;

import com.example.demo001.domain.Products.Product;
import com.example.demo001.domain.Products.ProductType;
import com.example.demo001.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<Product> FindProductsByProductType(ProductType productType) {
        return productRepository.getAllByProductType(productType);
    }

    @Override
    public Product FindProductByName(String productName) {
        return productRepository.getByProductName(productName);
    }
}
package com.example.demo001.service;

import com.example.demo001.domain.Products.Product;
import com.example.demo001.domain.Products.ProductType;

import java.util.List;

public interface ProductService {
    List<Product> FindProductsByProductType(ProductType productType); //ProductType to jest enum na typy produktów !
}

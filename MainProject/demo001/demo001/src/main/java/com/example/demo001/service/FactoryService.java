package com.example.demo001.service;

import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.Factory.ProductionAbility;
import com.example.demo001.domain.Products.Product;
import com.example.demo001.domain.Products.ProductType;

import java.util.List;

public interface FactoryService {
    Factory findByName(String name);

    void deleteProductionAbilityFromFactory(Factory factory, ProductionAbility productionAbility);

}

package com.example.demo001.service;

import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.Factory.ProductionAbility;
import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.Products.Product;

import java.util.List;

//Dawniej FactoryForOrderItemSetup
public interface ProductionAbilityService {

    List<Factory> searchAvailableFactories(OrderItem orderItem);

    void setupProduction(Factory factoryToProduce, OrderItem itemToBeProvided);

    void printProductAbility();

    Factory getFactoryByName(String name);

    void newProductionAbilityForFactory(Factory factory, ProductionAbility productionAbility);

    void updateProductionAbilityAmount(ProductionAbility productionAbility);

    void deleteProductionAbility(ProductionAbility productionAbility);
}

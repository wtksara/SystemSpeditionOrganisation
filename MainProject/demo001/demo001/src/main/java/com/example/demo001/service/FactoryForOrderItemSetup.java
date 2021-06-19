package com.example.demo001.service;

import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.Products.Product;

import java.util.List;

public interface FactoryForOrderItemSetup {

    List<Factory> searchAvailableFactories(OrderItem orderItem);

    void setupProduction(Factory factoryToProduce, OrderItem itemToBeProvided);

}

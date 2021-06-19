package com.example.demo001.service;

import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.OrderManagement.OrderItem;

import java.util.List;

public interface OrderItemService {
    ServiceErrorCode CreateOrderItem(OrderItem orderItem);
    List<OrderItem> FindOrderItemsByOrder(long orderID);
    ServiceErrorCode setFactoryForOrderItem(OrderItem actualOrderItem, Factory chosenFactory);
    List<Factory> getPossibleFactories(OrderItem actualOrderItem);//lista fabryk w których można wyprodukować produkt z tego OrderItem
}

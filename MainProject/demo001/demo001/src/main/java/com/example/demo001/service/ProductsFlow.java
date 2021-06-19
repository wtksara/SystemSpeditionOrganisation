package com.example.demo001.service;

import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.Factory.ProductionAbility;
import com.example.demo001.domain.OrderManagement.OrderItem;

public interface ProductsFlow {

    void produceNewProducts(ProductionAbility productionAbility, int producedProducts);
    void reserveAllProducedItemsInFactory(Factory factory, int productionReservationStep);
    void reserveCertainProductInFactory(OrderItem orderItem, int productionReservationStep);
    void rollbackCertainProductReservation(OrderItem orderItem, int productionReservationRollbackStep);
    void moveItemsToOrderItem(OrderItem orderItem);
}

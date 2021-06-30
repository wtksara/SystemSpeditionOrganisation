package com.example.demo001.service;

import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.repository.FactoryRepository;
import com.example.demo001.repository.OrderItemRepository;
import com.example.demo001.repository.ProductionAbilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService{

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductionAbilityRepository productionAbilityRepository;

    @Autowired
    private FactoryRepository factoryRepository;

    @Override
    public ServiceErrorCode CreateOrderItem(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
        return ServiceErrorCode.OK;
    }

    @Override
    public List<OrderItem> FindOrderItemsByOrder(long orderID) {
        return orderItemRepository.getOrderItemsByOrder_OrderId(orderID);
    }

    @Override
    public ServiceErrorCode setFactoryForOrderItem(OrderItem actualOrderItem, Factory chosenFactory) {
        actualOrderItem.setFactory(chosenFactory);
        return ServiceErrorCode.OK;
    }

    @Override
    public List<Factory> getPossibleFactories(OrderItem actualOrderItem) {
        return factoryRepository.getFactoryByProducedProductsContains(productionAbilityRepository.getByMyProduct(actualOrderItem.getProduct()));
    }
}

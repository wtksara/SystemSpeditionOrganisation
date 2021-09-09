package com.example.demo001.service;

import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.repository.FactoryRepository;
import com.example.demo001.repository.OrderItemRepository;
import com.example.demo001.repository.ProductionAbilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        List<OrderItem> orderItems = orderItemRepository.getOrderItemsByOrder_OrderId(orderID);
        return orderItems;
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

    @Override
    public ServiceErrorCode modifyOrderItem(OrderItem orderItem) {
        Optional<OrderItem> ord = orderItemRepository.getOrderItemById(orderItem.getId());
        if(!ord.isPresent()) {
            return ServiceErrorCode.NOT_FOUND;
        }
        OrderItem pord = ord.get();
        pord.setFactory(orderItem.getFactory());
        if(pord == null) {
            return ServiceErrorCode.INTERNAL_SERVER_ERROR;
        }
        return ServiceErrorCode.OK;
    }
}

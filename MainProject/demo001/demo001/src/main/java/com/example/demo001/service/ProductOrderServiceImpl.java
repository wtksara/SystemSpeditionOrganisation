package com.example.demo001.service;

import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.OrderManagement.*;
import com.example.demo001.repository.ProductOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Override
    public ProductOrder createOrder(ProductOrder order) {
        return productOrderRepository.save(order);
    }

    @Override
    public ServiceErrorCode modifyOrder(ProductOrder order) {
        Optional<ProductOrder> ord = productOrderRepository.findProductOrderByOrderId(order.getOrderId());
        if(!ord.isPresent()) {
            return ServiceErrorCode.NOT_FOUND;
        }
        ProductOrder pord = ord.get();
        pord.setOrderStatus(order.getOrderStatus());
        pord.setOrderTransportProvider(pord.getOrderTransportProvider());
        pord = productOrderRepository.save(pord);
        if(pord == null) {
            return ServiceErrorCode.INTERNAL_SERVER_ERROR;
        }
        return ServiceErrorCode.OK;
    }

    @Override
    public List<ProductOrder> findCurrentOrders(String clientName) {
        List<ProductOrder> results = productOrderRepository
                .findProductOrderByOrderClientAndOrderStatus(clientName, OrderStatus.ISSUED);
        results.addAll(productOrderRepository
                .findProductOrderByOrderClientAndOrderStatus(clientName, OrderStatus.LKN_4_TP));
        results.addAll(productOrderRepository
                .findProductOrderByOrderClientAndOrderStatus(clientName, OrderStatus.PENDING_TP));
        results.addAll(productOrderRepository
                .findProductOrderByOrderClientAndOrderStatus(clientName, OrderStatus.OFFER_SENT));
        results.addAll(productOrderRepository
                .findProductOrderByOrderClientAndOrderStatus(clientName, OrderStatus.ACCEPTED));
        results.addAll(productOrderRepository
                .findProductOrderByOrderClientAndOrderStatus(clientName, OrderStatus.COMPLETED));
        results.addAll(productOrderRepository
                .findProductOrderByOrderClientAndOrderStatus(clientName, OrderStatus.IN_TRANSPORT));
        return results;
    }

    @Override
    public List<ProductOrder> findHistoricOrders(String clientName) {
        List<ProductOrder> results = productOrderRepository
                .findProductOrderByOrderClientAndOrderStatus(clientName, OrderStatus.DELIVERED);
        results.addAll(productOrderRepository
                .findProductOrderByOrderClientAndOrderStatus(clientName, OrderStatus.REJECTED));
        return results;
    }

    @Override
    public List<ProductOrder> findOrdersByTransportProvider(String transportProviderName) {
        List<ProductOrder> results = productOrderRepository
                .findProductOrderByOrderTransportProviderAndOrderStatus
                        (transportProviderName, OrderStatus.COMPLETED);
        results.addAll(productOrderRepository
                .findProductOrderByOrderTransportProviderAndOrderStatus
                        (transportProviderName, OrderStatus.IN_TRANSPORT));
        results.addAll(productOrderRepository
                .findProductOrderByOrderTransportProviderAndOrderStatus
                        (transportProviderName, OrderStatus.DELIVERED));
        return results;
    }

    @Override
    public List<ProductOrder> findPendingOrdersByTransportProvider(String transportProviderName) {
        return productOrderRepository
                .findProductOrderByOrderTransportProviderAndOrderStatus
                        (transportProviderName, OrderStatus.PENDING_TP);
    }

    @Override
    public List<ProductOrder> findOrdersForManagements() {
        List<ProductOrder> results = productOrderRepository.findProductOrdersByOrderStatus(OrderStatus.ISSUED);
        results.addAll(productOrderRepository.findProductOrdersByOrderStatus(OrderStatus.LKN_4_TP));
        return results;
    }

}
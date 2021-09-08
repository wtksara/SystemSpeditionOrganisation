package com.example.demo001.service;

import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.OrderManagement.OrderManager;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.domain.Transport.TransportProvider;

import java.util.List;

public interface TransportProviderService {
    List<TransportProvider> getPossibleTransportProvidersForOrderItem(ProductOrder productOrder, OrderItem orderItem);
    List<TransportProvider> getPossibleTransportProvidersForProductOrder(long productOrderId);
    public List<TransportProvider> findAll();
    TransportProvider findByUsername(String username);
    boolean addUser(TransportProvider bUser);
    boolean saveChangedUser(TransportProvider bUser);
}

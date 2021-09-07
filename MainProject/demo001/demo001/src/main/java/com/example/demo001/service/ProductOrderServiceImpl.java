package com.example.demo001.service;

import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.OrderManagement.*;
import com.example.demo001.repository.ProductOrderRepository;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.ArrayList;
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
    public List<ProductOrder> findAllHistoricOrders(){
        List<ProductOrder> results = productOrderRepository
                .findProductOrdersByOrderStatus(OrderStatus.DELIVERED);
        results.addAll(productOrderRepository
                .findProductOrdersByOrderStatus(OrderStatus.REJECTED));
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

    @Override
    public List<ProductOrder> findActualOrdersByTransportProvider(String transportProviderName) //Dodane przez Pawla
    {
        List<ProductOrder> results = new ArrayList<ProductOrder>();
        List<ProductOrder> allOrders = productOrderRepository.findAll();
        for(ProductOrder order : allOrders){
            if(order.getOrderTransportProvider().getUserName().equals(transportProviderName)&& order.getOrderStatus()==OrderStatus.IN_TRANSPORT)
            {
                results.add(order);
            }
        }
        return results;
    }

    @Override
    public List<ProductOrder> findPendingOrdersForTransportProviderByTransportProvider(String transportProviderName) //Dodane przez Pawla
    {
        List<ProductOrder> results = new ArrayList<ProductOrder>();
        List<ProductOrder> allOrders = productOrderRepository.findAll();
        for(ProductOrder order : allOrders){
            if(order.getOrderTransportProvider().getUserName().equals(transportProviderName)&& order.getOrderStatus()==OrderStatus.PENDING_TP)
            {
                results.add(order);
            }
        }
        return results;
    }

    @Override
    public boolean saveChangedOrder (ProductOrder order) //Dodane przez Pawla
    {
        ProductOrder order1 = productOrderRepository.save(order);
        return order1 != null;
    }

    @Override
    public void deleteAllOrders() //Dodane przez Pawla
    {
        productOrderRepository.deleteAll();
    }

    @Override
    public ProductOrder findOrderByID(Long ID) //Dodane przez Pawla
    {
        List<ProductOrder> allProductOrders = productOrderRepository.findAll();
        ProductOrder pOrder;
        for (ProductOrder productOrders : allProductOrders) {
            if (productOrders.getOrderId()==ID) {
                pOrder = productOrders;
                return pOrder;
            }
        }
        return null;
    }

}
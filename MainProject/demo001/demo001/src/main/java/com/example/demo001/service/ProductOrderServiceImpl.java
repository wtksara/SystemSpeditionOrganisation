package com.example.demo001.service;

import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.OrderManagement.OrderStatus;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.domain.Transport.TransportProvider;
import com.example.demo001.repository.ProductOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        pord.setOrderTransportProvider(order.getOrderTransportProvider());
        pord = productOrderRepository.save(pord);
        if(pord == null) {
            return ServiceErrorCode.INTERNAL_SERVER_ERROR;
        }
        return ServiceErrorCode.OK;
    }

    @Override
    public List<ProductOrder> findCurrentOrders(Client clientName) {
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
        //return productOrderRepository.findAll();
    }

    @Override
    public List<ProductOrder> findHistoricOrders(Client clientName) {
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
        results.addAll(productOrderRepository
                .findProductOrdersByOrderStatus(OrderStatus.PENDING_TP));
        results.addAll(productOrderRepository
                .findProductOrdersByOrderStatus(OrderStatus.OFFER_SENT));
        results.addAll(productOrderRepository
                .findProductOrdersByOrderStatus(OrderStatus.ACCEPTED));
        results.addAll(productOrderRepository
                .findProductOrdersByOrderStatus(OrderStatus.COMPLETED));
        results.addAll(productOrderRepository
                .findProductOrdersByOrderStatus(OrderStatus.IN_TRANSPORT));
        return results;
    }

    @Override
    public List<ProductOrder> findOrdersByTransportProvider(TransportProvider transportProvider) {
        List<ProductOrder> results = productOrderRepository
                .findProductOrderByOrderTransportProviderAndOrderStatus
                        (transportProvider, OrderStatus.COMPLETED);
        results.addAll(productOrderRepository
                .findProductOrderByOrderTransportProviderAndOrderStatus
                        (transportProvider, OrderStatus.IN_TRANSPORT));
        results.addAll(productOrderRepository
                .findProductOrderByOrderTransportProviderAndOrderStatus
                        (transportProvider, OrderStatus.DELIVERED));
        results.addAll(productOrderRepository
                .findProductOrderByOrderTransportProviderAndOrderStatus
                        (transportProvider, OrderStatus.ACCEPTED));
        return results;
    }

    @Override
    public List<ProductOrder> findPendingOrdersByTransportProvider(TransportProvider transportProvider) {
        return productOrderRepository
                .findProductOrderByOrderTransportProviderAndOrderStatus
                        (transportProvider, OrderStatus.PENDING_TP);
    }

    @Override
    public List<ProductOrder> findOrdersForManagements() {
        List<ProductOrder> results = productOrderRepository.findProductOrdersByOrderStatus(OrderStatus.ISSUED);
        results.addAll(productOrderRepository.findProductOrdersByOrderStatus(OrderStatus.LKN_4_TP));
        return results;
    }

    @Override
    public List<ProductOrder> findActualOrdersByTransportProvider(TransportProvider transportProvider) //Dodane przez Pawla
    {
        List<ProductOrder> results = new ArrayList<ProductOrder>();
        List<ProductOrder> allOrders = productOrderRepository.findAll();
        for(ProductOrder order : allOrders){
            if(order.getOrderTransportProvider().getUserName().equals(transportProvider.getUserName())&& order.getOrderStatus()==OrderStatus.IN_TRANSPORT)
            {
                results.add(order);
            }
        }
        return results;
    }

    @Override
    public List<ProductOrder> findPendingOrdersForTransportProviderByTransportProvider(TransportProvider transportProviderName) //Dodane przez Pawla
    {


        List<ProductOrder> results = new ArrayList<ProductOrder>();
        List<ProductOrder> allOrders = productOrderRepository.findAll();
        for(ProductOrder order : allOrders){
            if(order.getOrderTransportProvider().getUserName().equals(transportProviderName.getUserName())&& order.getOrderStatus()==OrderStatus.PENDING_TP)
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
    @Transactional
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
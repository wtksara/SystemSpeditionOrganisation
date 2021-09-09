package com.example.demo001.service;

import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.OrderManagement.*;
import com.example.demo001.domain.Transport.TransportProvider;

import java.util.List;

public interface ProductOrderService {

    ProductOrder createOrder(ProductOrder order); //wysłanie zamówienia do bazy
    ServiceErrorCode modifyOrder(ProductOrder order); //zmiana informacji w zamówieniu*/
    List<ProductOrder> findCurrentOrders(Client clientName); //pobranie wszystkich zamówień o danym statusie
    List<ProductOrder> findHistoricOrders(Client clientName);
    List<ProductOrder> findOrdersByTransportProvider(TransportProvider transportProvider);

    List<ProductOrder> findActualOrdersByTransportProvider(TransportProvider transportProvider); //Dodane przez Pawla
    List<ProductOrder> findPendingOrdersForTransportProviderByTransportProvider(TransportProvider transportProvider); //Dodane przez Pawla
    boolean saveChangedOrder (ProductOrder order); //Dodane przez Pawla
    void deleteAllOrders(); //Dodane przez Pawla
    ProductOrder findOrderByID(Long ID); //Dodane przez Pawla

    List<ProductOrder> findPendingOrdersByTransportProvider(TransportProvider transportProvider);
    List<ProductOrder> findOrdersForManagements(); //wszystkie ProductOrder o statusie do rozporządzenia nimi
    List<ProductOrder> findAllHistoricOrders();//Piotr
}
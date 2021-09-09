package com.example.demo001.service;

import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.OrderManagement.OrderManager;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.domain.Transport.City;
import com.example.demo001.domain.Transport.Connection;
import com.example.demo001.domain.Transport.TransportProvider;
import com.example.demo001.gui_controller.NavigationController;
import com.example.demo001.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransportProviderServiceImpl implements TransportProviderService{

    @Autowired
    private TransportProviderRepository transportProviderRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private BasicUserRepository basicUserRepository;

    @Autowired
    private FactoryRepository factoryRepository;


    @Override
    public List<TransportProvider> getPossibleTransportProvidersForOrderItem(ProductOrder productOrder, OrderItem orderItem) {
        /*Optional<ProductOrder> productOrderOptional = this.productOrderRepository.findProductOrderByOrderId(productOrderId);
        ProductOrder productOrder = productOrderOptional.get();
        List<OrderItem> orderItems = this.orderItemRepository.getOrderItemsByOrder_OrderId(productOrderId);*/
        Client client = (Client)basicUserRepository.findById(productOrder.getOrderClient().getUserId()).get();
        City clientCity = client.getClientCity();
        City factoryCity = orderItem.getFactory().getFactoryLocation();
        Connection connection = connectionRepository
                .getByFirstCityEqualsAndSecondCityEquals(clientCity, factoryCity);
        List<TransportProvider> possibleTransportProviders = transportProviderRepository
                .getAllByConnectionsContaining(connection);
        return possibleTransportProviders;

    }

    @Override
    public List<TransportProvider> getPossibleTransportProvidersForProductOrder(long productOrderId) {
        Optional<ProductOrder> productOrderOptional = this.productOrderRepository.findProductOrderByOrderId(productOrderId);
        ProductOrder productOrder = productOrderOptional.get();

        List<TransportProvider> possibleTransportProviders = new ArrayList<>();


        for(OrderItem orderItem : NavigationController.offerOrderItems){
            possibleTransportProviders.addAll(getPossibleTransportProvidersForOrderItem(productOrder, orderItem));
        }

        return possibleTransportProviders;
    }

    @Override
    public List<TransportProvider> findAll(){
       return transportProviderRepository.findAll();
    }

    @Override
    public TransportProvider findByUsername(String username)
    {
        List<TransportProvider> allBasicUsers = transportProviderRepository.findAll();
        for (TransportProvider allBasicUser : allBasicUsers) {
            if (Objects.equals(allBasicUser.getUserName(), username))
                return allBasicUser;
        }
        return null;
    }

    @Override
    public boolean addUser(TransportProvider bUser)
    {
        TransportProvider bb = transportProviderRepository.save(bUser);
        return bb != null;
    }

    @Override
    public boolean saveChangedUser(TransportProvider bUser)
    {
        TransportProvider bb = transportProviderRepository.save(bUser);
        return bb != null;
    }

}

package com.example.demo001.service;

import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.domain.Transport.Connection;
import com.example.demo001.domain.Transport.TransportProvider;
import com.example.demo001.repository.ConnectionRepository;
import com.example.demo001.repository.TransportProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportProviderServiceImpl implements TransportProviderService{

    @Autowired
    private TransportProviderRepository transportProviderRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Override
    public List<TransportProvider> getPossibleTransportProvidersForOrderItem(ProductOrder productOrder, OrderItem orderItem) {
                return transportProviderRepository
                    .getAllByConnectionsContaining(connectionRepository
                            .getByFirstCityEqualsAndSecondCityEquals(productOrder.getOrderClient().getClientCity(),
                                    orderItem.getFactory().getFactoryLocation()));

    }

    @Override
    public List<TransportProvider> findAll(){
       return transportProviderRepository.findAll();
    }
}

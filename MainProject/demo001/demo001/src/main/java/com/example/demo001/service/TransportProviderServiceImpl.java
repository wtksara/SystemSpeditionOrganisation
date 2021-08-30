package com.example.demo001.service;

import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.OrderManagement.OrderManager;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.domain.Transport.Connection;
import com.example.demo001.domain.Transport.TransportProvider;
import com.example.demo001.repository.ConnectionRepository;
import com.example.demo001.repository.TransportProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

package com.example.demo001.service;


import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Factory.FactoryManager;
import com.example.demo001.domain.OrderManagement.OrderManager;
import com.example.demo001.repository.BasicUserRepository;
import com.example.demo001.repository.OrderManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderManagerServiceImpl implements OrderManagerService {

    @Autowired
    private OrderManagerRepository orderManagerRepository;


    @Override
    public OrderManager findByUsername(String username)
    {
        List<OrderManager> allBasicUsers = orderManagerRepository.findAll();
        for (OrderManager allBasicUser : allBasicUsers) {
            if (Objects.equals(allBasicUser.getUserName(), username))
                return allBasicUser;
        }
        return null;
    }

    @Override
    public boolean addUser(OrderManager bUser)
    {
        OrderManager bb = orderManagerRepository.save(bUser);
        return bb != null;
    }

    @Override
    public boolean saveChangedUser(OrderManager bUser)
    {
        OrderManager bb = orderManagerRepository.save(bUser);
        return bb != null;
    }

}

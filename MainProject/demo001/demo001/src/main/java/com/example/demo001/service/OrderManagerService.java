package com.example.demo001.service;


import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.OrderManagement.OrderManager;

import java.util.List;

public interface OrderManagerService {
      OrderManager findByUsername(String username);
      boolean addUser(OrderManager bUser);
      boolean saveChangedUser(OrderManager bUser);
}

package com.example.demo001.service;


import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Factory.FactoryManager;

import java.util.List;

public interface FactoryManagerService {
      List<FactoryManager> findAll();
      FactoryManager findByUsername(String username);
      boolean addUser(FactoryManager bUser);
      boolean saveChangedUser(FactoryManager bUser);
}

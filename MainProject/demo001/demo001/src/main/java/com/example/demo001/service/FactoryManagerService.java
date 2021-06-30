package com.example.demo001.service;


import com.example.demo001.domain.Factory.FactoryManager;

import java.util.List;

public interface FactoryManagerService {
      List<FactoryManager> findAll();
      FactoryManager findByUsername(String username);
}

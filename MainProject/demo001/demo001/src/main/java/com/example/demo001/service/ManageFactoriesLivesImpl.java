package com.example.demo001.service;

import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.repository.FactoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManageFactoriesLivesImpl implements ManageFactoriesLives {

    @Autowired
    private FactoryRepository factoryRepository;

    @Override
    public void createFactory(String factoryName) {
        factoryRepository.save(new Factory(factoryName));
    }

    @Override
    public void deleteFactory(String factoryName) {
        factoryRepository.deleteFactoryByFactoryName(factoryName);
    }
}

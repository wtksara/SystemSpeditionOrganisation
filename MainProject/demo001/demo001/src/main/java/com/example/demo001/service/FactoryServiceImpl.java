package com.example.demo001.service;

import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.repository.FactoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FactoryServiceImpl implements FactoryService {

    @Autowired
    private FactoryRepository factoryRepository;

    @Override
    public Factory findByName(String name)
    {
        List<Factory> allFactories = factoryRepository.findAll();
        for (Factory allFactory : allFactories) {
            if (Objects.equals(allFactory.getFactoryName(), name))
                return allFactory;
        }
        return null;
    }
}

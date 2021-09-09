package com.example.demo001.service;

import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.Factory.ProductionAbility;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.repository.FactoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Override
    public void deleteProductionAbilityFromFactory(Factory factory, ProductionAbility productionAbility) {
        Factory modifiedFactory = factoryRepository.getFactoryByFactoryName(factory.getFactoryName());
        List<ProductionAbility> factoryProducts = factory.getProducedProducts();
        factoryProducts.remove(productionAbility);
        modifiedFactory.setProducedProducts(factoryProducts);
        factoryRepository.save(modifiedFactory);
        /*Factory modifiedFactory = factoryRepository.getFactoryByFactoryName(factory.getFactoryName());
        modifiedFactory.setProducedProducts(factory.getProducedProducts());*/
    }
}

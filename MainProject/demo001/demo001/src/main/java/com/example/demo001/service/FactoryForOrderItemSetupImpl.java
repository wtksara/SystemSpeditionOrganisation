package com.example.demo001.service;

import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.Factory.ProductionAbility;
import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.Products.Product;
import com.example.demo001.repository.FactoryRepository;
import com.example.demo001.repository.ProductionAbilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FactoryForOrderItemSetupImpl implements FactoryForOrderItemSetup {

    @Autowired
    private FactoryRepository factoryRepository;

    @Autowired
    private ProductionAbilityRepository productionAbilityRepository;

    @Override
    public List<Factory> searchAvailableFactories(OrderItem orderItem) {
        return factoryRepository.findAll().stream().filter(x ->
                x.getProducedProducts().stream().anyMatch(y -> y.getMyProduct() == orderItem.getProduct()))
                .collect(Collectors.toList());

               /* productionAbilityRepository.getAllByMyProduct(orderItem.getProduct())
                .stream().map(ProductionAbility::getMyFactory).collect(Collectors.toList());*/
    }

    @Override
    public void setupProduction(Factory factoryToProduce, OrderItem itemToBeProvided) {
        itemToBeProvided.setFactory(factoryToProduce);
    }
}

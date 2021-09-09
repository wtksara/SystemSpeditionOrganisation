package com.example.demo001.service;

import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.Factory.FactoryManager;
import com.example.demo001.domain.Factory.ProductionAbility;
import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.Products.Product;
import com.example.demo001.gui_controller.NavigationController;
import com.example.demo001.repository.FactoryRepository;
import com.example.demo001.repository.ProductionAbilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductionAbilityServiceImpl implements ProductionAbilityService {

    @Autowired
    private FactoryRepository factoryRepository;

    @Autowired
    private ProductionAbilityRepository productionAbilityRepository;

    @Override
    public List<Factory> searchAvailableFactories(OrderItem orderItem) {
        List<Factory> factories = factoryRepository.findAll();
        List<Factory> possibleFactories = new ArrayList<>();
        for(Factory fact : factories){
            List<ProductionAbility> productionAbilities = fact.getProducedProducts();
            for(ProductionAbility prodAb : productionAbilities){
                if(prodAb.getMyProduct().getProductName().equals(orderItem.getProduct().getProductName())){
                    possibleFactories.add(fact);
                }
            }
        }
        return possibleFactories;
        /*return factoryRepository.findAll().stream().filter(x ->
                x.getProducedProducts().stream().anyMatch(y -> y.getMyProduct() == orderItem.getProduct()))
                .collect(Collectors.toList());*/

               /* productionAbilityRepository.getAllByMyProduct(orderItem.getProduct())
                .stream().map(ProductionAbility::getMyFactory).collect(Collectors.toList());*/
    }


    @Override
    public void setupProduction(Factory factoryToProduce, OrderItem itemToBeProvided) {
        itemToBeProvided.setFactory(factoryToProduce);
    }


    @Override
    public void printProductAbility() {
        factoryRepository.deleteFactoryByFactoryName("test");
        factoryRepository.save(new Factory("test"));
        List<Factory> factories = factoryRepository.findAll();
        Factory myFactory = factoryRepository.getFactoryByFactoryName("test");
        /*for (ProductionAbility prodAb : myFactory.getProducedProducts()){
            System.out.println(prodAb.getMyProduct().getProductName());
        }*/
    }

    @Override
    public Factory getFactoryByName(String name) {
        return factoryRepository.getFactoryByFactoryName(name);
    }

    @Override
    public void newProductionAbilityForFactory(Factory factory, ProductionAbility productionAbility) {
        productionAbilityRepository.save(productionAbility);
        Factory fact = factoryRepository.getFactoryByFactoryName(factory.getFactoryName());
        List<ProductionAbility> newProd = fact.getProducedProducts();
        newProd.add(productionAbility);
        fact.setProducedProducts(newProd);
        factoryRepository.save(fact);
    }

    @Override
    public void updateProductionAbilityAmount(ProductionAbility productionAbility) {
        ProductionAbility changedProductionAbility = productionAbilityRepository.getProductionAbilityById(productionAbility.getId());
        changedProductionAbility.setProductAmount(productionAbility.getProductAmount());
        productionAbilityRepository.save(changedProductionAbility);
    }

    @Override
    public ProductionAbility getProductionAbilityAmountByProductAndFactory(Factory factory, Product product) {
        Factory factoryToModify = factoryRepository.getFactoryByFactoryName(factory.getFactoryName());
        for(ProductionAbility productionAbility : factoryToModify.getProducedProducts()){
            if(productionAbility.getMyProduct().getProductName().equals(product.getProductName())){
                return productionAbility;
            }
        }
        return null;
    }

    @Override
    public void deleteProductionAbility(ProductionAbility productionAbility) {
        //this.factoryRepository.deleteFactoryByFactoryName(factory.getFactoryName());
        productionAbilityRepository.deleteById(productionAbility.getId());
    }

}

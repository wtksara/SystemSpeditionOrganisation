package com.example.demo001.service;

import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.Factory.ProductionAbility;
import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.repository.FactoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductsFlowImpl implements ProductsFlow {

    @Autowired
    private FactoryRepository factoryRepository;

    @Override
    public void produceNewProducts(ProductionAbility productionAbility, int producedProducts) {
        productionAbility.setProductAmount(productionAbility.getProductAmount() + producedProducts);
    }

    @Override
    public void reserveAllProducedItemsInFactory(Factory factory, int productionReservationStep) {
        for (ProductionAbility productionAbility : factory.getProducedProducts())
            if (productionAbility.getProductAmount() >= productionReservationStep) {
                productionAbility.setProductAmount((productionAbility.getProductAmount() - productionReservationStep));
                productionAbility.setReservedProducts(productionAbility.getReservedProducts() + productionReservationStep);
            }
    }

    @Override
    public void reserveCertainProductInFactory(OrderItem orderItem, int productionReservationStep) {
        ProductionAbility productionAbility = orderItem.getFactory().getProducedProducts().stream()
                .filter(x -> x.getMyProduct() == orderItem.getProduct()).findFirst().get();

        if (productionAbility.getProductAmount() >= productionReservationStep) {
            productionAbility.setProductAmount((productionAbility.getProductAmount() - productionReservationStep));
            productionAbility.setReservedProducts(productionAbility.getReservedProducts() + productionReservationStep);
        }
    }

    @Override
    public void rollbackCertainProductReservation(OrderItem orderItem, int productionReservationRollbackStep) {
        ProductionAbility productionAbility = orderItem.getFactory().getProducedProducts().stream()
                .filter(x -> x.getMyProduct().equals(orderItem.getProduct())).findFirst().get();

        if (productionAbility.getReservedProducts() >= productionReservationRollbackStep) {
            productionAbility.setProductAmount((productionAbility.getProductAmount() + productionReservationRollbackStep));
            productionAbility.setReservedProducts(productionAbility.getReservedProducts() - productionReservationRollbackStep);
        }
    }

    @Override
    public void moveItemsToOrderItem(OrderItem orderItem) {
        ProductionAbility production = orderItem.getFactory()
                .getProducedProducts().stream().filter(x -> x.getMyProduct().equals(orderItem.getProduct()))
                .findFirst().get();
        if(orderItem.getProductAmount() <= production.getReservedProducts()){
            production.setReservedProducts(production.getReservedProducts() - orderItem.getProductAmount());
            orderItem.setProductAmount(0);
            //TODO Update related Order status
        }
    }
}

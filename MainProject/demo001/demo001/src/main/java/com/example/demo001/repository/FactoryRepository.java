package com.example.demo001.repository;

import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.Factory.FactoryManager;
import com.example.demo001.domain.Factory.ProductionAbility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactoryRepository extends JpaRepository<Factory, Long> {

    void deleteFactoryByFactoryName(String factoryName);

    List<Factory> getFactoryByProducedProductsContains(ProductionAbility productionAbility);

    Factory getFactoryByFactoryName(String factoryName);

}

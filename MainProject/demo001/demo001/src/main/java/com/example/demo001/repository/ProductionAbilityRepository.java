package com.example.demo001.repository;

import com.example.demo001.domain.Factory.ProductionAbility;
import com.example.demo001.domain.Products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionAbilityRepository extends JpaRepository<ProductionAbility, Long> {

    ProductionAbility getByMyProduct(Product product);

    ProductionAbility getProductionAbilityById(long id);
}

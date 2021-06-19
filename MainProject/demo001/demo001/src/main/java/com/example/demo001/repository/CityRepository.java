package com.example.demo001.repository;


import com.example.demo001.domain.Factory.ProductionAbility;
import com.example.demo001.domain.Transport.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}

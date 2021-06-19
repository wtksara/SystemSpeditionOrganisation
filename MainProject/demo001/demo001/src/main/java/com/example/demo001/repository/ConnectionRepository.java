package com.example.demo001.repository;

import com.example.demo001.domain.Transport.City;
import com.example.demo001.domain.Transport.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    Connection getByFirstCityEqualsAndSecondCityEquals(City firstCity, City secondCity);
}

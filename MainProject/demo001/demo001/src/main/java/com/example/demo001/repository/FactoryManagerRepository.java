package com.example.demo001.repository;


import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Factory.FactoryManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactoryManagerRepository extends JpaRepository<FactoryManager, Long> {

}

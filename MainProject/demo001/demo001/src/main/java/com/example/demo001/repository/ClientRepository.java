package com.example.demo001.repository;


import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}

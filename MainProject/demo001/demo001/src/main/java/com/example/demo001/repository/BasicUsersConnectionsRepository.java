package com.example.demo001.repository;


import com.example.demo001.domain.Actors.BasicUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasicUsersConnectionsRepository extends JpaRepository<BasicUser, Long> {

}

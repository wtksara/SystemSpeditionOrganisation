package com.example.demo001.repository;

import com.example.demo001.domain.Transport.Connection;
import com.example.demo001.domain.Transport.TransportProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportProviderRepository extends JpaRepository<TransportProvider, Long> {
    List<TransportProvider> getAllByConnectionsContaining(Connection connection);
}

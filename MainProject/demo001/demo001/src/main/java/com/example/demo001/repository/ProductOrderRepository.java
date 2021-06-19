package com.example.demo001.repository;

import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.OrderManagement.*;
import com.example.demo001.domain.Transport.TransportProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

    Optional<ProductOrder> findProductOrderByOrderId(long orderId);

    List<ProductOrder> findProductOrderByOrderTransportProviderAndOrderStatus(String transportProviderName, OrderStatus orderStatus);

    List<ProductOrder> findProductOrderByOrderClientAndOrderStatus(String clientName, OrderStatus orderStatus);

    List<ProductOrder> findProductOrdersByOrderStatus(OrderStatus orderStatus);

}

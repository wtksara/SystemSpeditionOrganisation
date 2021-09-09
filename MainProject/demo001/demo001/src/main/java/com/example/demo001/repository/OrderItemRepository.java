package com.example.demo001.repository;

import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.Factory.ProductionAbility;
import com.example.demo001.domain.OrderManagement.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> getOrderItemsByOrder_OrderId(long orderID);
    Optional<OrderItem> getOrderItemById(long orderItemId);
}

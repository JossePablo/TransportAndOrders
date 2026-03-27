package com.empresa.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empresa.api.model.entity.Order;
import com.empresa.api.model.entity.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    // Listar filtros
    List<Order> findByStatus(OrderStatus status);
}

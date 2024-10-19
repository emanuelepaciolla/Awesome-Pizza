package com.exercise.pizzeria.repository;

import com.exercise.pizzeria.entity.Order;
import com.exercise.pizzeria.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findFirstByStatus(OrderStatus status);

    List<Order> findAllByOrderByCreatedDate();

    List<Order> findAllByStatusOrderByCreatedDate(OrderStatus status);

}
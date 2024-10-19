package com.exercise.pizzeria.repository;

import com.exercise.pizzeria.model.OrderResponseDTO;
import com.exercise.pizzeria.model.OrderStatus;
import com.exercise.pizzeria.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findFirstByStatus(OrderStatus status);

    @Query("""
            SELECT
             new com.exercise.pizzeria.model.OrderResponseDTO(ordine.id, ordine.customerName, ordine.pizzaType, ordine.status)
            FROM ordine ordine
            ORDER BY ordine.createdDate """)
    List<OrderResponseDTO> findAllOrders();

    @Query("""
        SELECT
         new com.exercise.pizzeria.model.OrderResponseDTO(ordine.id, ordine.customerName, ordine.pizzaType, ordine.status)
        FROM ordine ordine
        WHERE ordine.status = :status
        ORDER BY ordine.createdDate """)
    List<OrderResponseDTO> findAllOrders(@Param("status") OrderStatus status);

}
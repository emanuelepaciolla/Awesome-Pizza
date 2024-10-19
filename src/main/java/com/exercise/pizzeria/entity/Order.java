package com.exercise.pizzeria.entity;

import com.exercise.pizzeria.model.OrderStatus;
import com.exercise.pizzeria.model.PizzaType;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity(name = "ordine")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;
    private String customerName;
    @Column(name = "pizza_type")
    @Enumerated(value = EnumType.STRING)
    private PizzaType pizzaType;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    public Long getId() {
        return id;
    }

    public Order setId(Long id) {
        this.id = id;
        return this;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Order setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public PizzaType getPizzaType() {
        return pizzaType;
    }

    public Order setPizzaType(PizzaType pizzaType) {
        this.pizzaType = pizzaType;
        return this;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Order setStatus(OrderStatus status) {
        this.status = status;
        return this;
    }
}
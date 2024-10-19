package com.exercise.pizzeria.model;

public class OrderResponseDTO {
    private Long id;
    private String customerName;
    private PizzaType pizzaType;
    private OrderStatus status;

    private Long nextOrderId;

    public OrderResponseDTO(Long id, String customerName, PizzaType pizzaType, OrderStatus status) {
        this.id = id;
        this.customerName = customerName;
        this.pizzaType = pizzaType;
        this.status = status;
    }

    public OrderResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public OrderResponseDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getCustomerName() {
        return customerName;
    }

    public OrderResponseDTO setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public PizzaType getPizzaType() {
        return pizzaType;
    }

    public OrderResponseDTO setPizzaType(PizzaType pizzaType) {
        this.pizzaType = pizzaType;
        return this;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public OrderResponseDTO setStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public Long getNextOrderId() {
        return nextOrderId;
    }

    public OrderResponseDTO setNextOrderId(Long nextOrderId) {
        this.nextOrderId = nextOrderId;
        return this;
    }

}

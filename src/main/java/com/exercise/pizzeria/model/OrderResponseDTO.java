package com.exercise.pizzeria.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class OrderResponseDTO {
    @Schema(description = "order id", type = "integer", example = "1")
    private Long id;
    @Schema(description = "customer name", type = "string", example = "Mario")
    private String customerName;
    @Schema(description = "pizza type", type = "enum", example = "MARGHERITA")
    private PizzaType pizzaType;
    @Schema(description = "order status", type = "enum", example = "IN_CODA")
    private OrderStatus status;

    @Schema(description = "next order id", type = "integer", example = "2")
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

package com.exercise.pizzeria.model;

import com.exercise.pizzeria.validation.OrderStatusDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class OrderStatusUpdateDTO {

    @Valid
    @NotNull(message = "orderStatus is required")
    @JsonDeserialize(using = OrderStatusDeserializer.class)
    @Schema(description = "order status", type = "enum", example = "IN_CODA", required = true)
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public OrderStatusUpdateDTO setStatus(OrderStatus status) {
        this.status = status;
        return this;
    }
}
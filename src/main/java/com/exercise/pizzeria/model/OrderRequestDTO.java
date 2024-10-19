package com.exercise.pizzeria.model;

import com.exercise.pizzeria.validation.PizzaTypeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class OrderRequestDTO {

    @Valid
    @NotEmpty(message = "customerName is required")
    @ApiModelProperty(value = "Mario", required = true)
    private String customerName;

    @Valid
    @NotNull(message = "pizzaType is required")
    @JsonDeserialize(using = PizzaTypeDeserializer.class)
    @ApiModelProperty(value = "MARGHERITA", required = true)
    private PizzaType pizzaType;

    public String getCustomerName() {
        return customerName;
    }

    public OrderRequestDTO setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public PizzaType getPizzaType() {
        return pizzaType;
    }

    public OrderRequestDTO setPizzaType(PizzaType pizzaType) {
        this.pizzaType = pizzaType;
        return this;
    }
}

package com.exercise.pizzeria.validation;

import com.exercise.pizzeria.exception.EnumNotValidException;
import com.exercise.pizzeria.model.OrderStatus;
import com.exercise.pizzeria.model.PizzaType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Arrays;

public class OrderStatusDeserializer extends JsonDeserializer<OrderStatus> {

    @Override
    public OrderStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText().toUpperCase();
        try {
            return OrderStatus.valueOf(value);
        } catch (Exception e) {
            throw new EnumNotValidException("Invalid orderStatus type: " + value + ". Valid values are: " + Arrays.toString(OrderStatus.values()));
        }
    }
}
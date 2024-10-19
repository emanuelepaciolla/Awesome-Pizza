package com.exercise.pizzeria.mapper;

import com.exercise.pizzeria.model.OrderResponseDTO;
import com.exercise.pizzeria.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponseDTO fromOrderToOrderResponseDTO(Order order);
}

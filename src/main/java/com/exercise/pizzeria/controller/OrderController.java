package com.exercise.pizzeria.controller;

import com.exercise.pizzeria.model.OrderRequestDTO;
import com.exercise.pizzeria.model.OrderResponseDTO;
import com.exercise.pizzeria.model.OrderStatus;
import com.exercise.pizzeria.model.OrderStatusUpdateDTO;
import com.exercise.pizzeria.exception.OrderNotFoundException;
import com.exercise.pizzeria.exception.StatusChangeNotValidException;
import com.exercise.pizzeria.usecase.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Api(value = "Order Management System", tags = "Order Management")
public class OrderController {

    private final OrderService orderUseCase;

    public OrderController(OrderService orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    @PostMapping
    @ApiOperation(value = "Create a new order", response = OrderResponseDTO.class)
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderRequestDTO orderRequest) {
        OrderResponseDTO savedOrder = orderUseCase.createOrder(orderRequest);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    @ApiOperation(value = "Get order by ID", response = OrderResponseDTO.class)
    public ResponseEntity<OrderResponseDTO> getOrderStatus(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderUseCase.getOrder(orderId), HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "Get all orders", response = List.class)
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(@RequestParam(value = "status", required = false) OrderStatus status) {
        return new ResponseEntity<>(orderUseCase.getAllOrders(status), HttpStatus.OK);
    }

    @PutMapping("/{orderId}/status")
    @ApiOperation(value = "Update order status", response = OrderResponseDTO.class)
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Long orderId,
                                                              @RequestBody @Valid OrderStatusUpdateDTO statusUpdate) throws OrderNotFoundException, StatusChangeNotValidException {
        return new ResponseEntity<>(orderUseCase.updateOrderStatus(orderId, statusUpdate.getStatus()), HttpStatus.OK);
    }

}

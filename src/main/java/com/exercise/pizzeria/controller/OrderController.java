package com.exercise.pizzeria.controller;

import com.exercise.pizzeria.model.OrderRequestDTO;
import com.exercise.pizzeria.model.OrderResponseDTO;
import com.exercise.pizzeria.model.OrderStatus;
import com.exercise.pizzeria.model.OrderStatusUpdateDTO;
import com.exercise.pizzeria.exception.OrderNotFoundException;
import com.exercise.pizzeria.exception.StatusChangeNotValidException;
import com.exercise.pizzeria.usecase.OrderService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderUseCase;

    public OrderController(OrderService orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    @PostMapping
    @Operation(summary = "Create a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderRequestDTO orderRequest) {
        OrderResponseDTO savedOrder = orderUseCase.createOrder(orderRequest);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderResponseDTO> getOrderStatus(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderUseCase.getOrder(orderId), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all orders")
    @ApiResponse(responseCode = "200", description = "List of orders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(@RequestParam(value = "status", required = false) OrderStatus status) {
        return new ResponseEntity<>(orderUseCase.getAllOrders(status), HttpStatus.OK);
    }

    @PutMapping("/{orderId}/status")
    @Operation(summary = "Update order status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Status change not valid")
    })
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Long orderId,
                                                              @RequestBody @Valid OrderStatusUpdateDTO statusUpdate) throws OrderNotFoundException, StatusChangeNotValidException {
        return new ResponseEntity<>(orderUseCase.updateOrderStatus(orderId, statusUpdate.getStatus()), HttpStatus.OK);
    }

}

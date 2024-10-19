package com.exercise.pizzeria.usecase;

import com.exercise.pizzeria.mapper.OrderMapper;
import com.exercise.pizzeria.model.OrderRequestDTO;
import com.exercise.pizzeria.model.OrderResponseDTO;
import com.exercise.pizzeria.model.OrderStatus;
import com.exercise.pizzeria.entity.Order;
import com.exercise.pizzeria.exception.OrderNotFoundException;
import com.exercise.pizzeria.exception.StatusChangeNotValidException;
import com.exercise.pizzeria.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    /**
     * Create an order
     * @param orderRequestDTO
     * @return
     */
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setCustomerName(orderRequestDTO.getCustomerName());
        order.setPizzaType(orderRequestDTO.getPizzaType());
        order.setStatus(OrderStatus.IN_CODA);
        Order savedOrder = orderRepository.save(order);
        return new OrderResponseDTO(savedOrder.getId(), savedOrder.getCustomerName(), savedOrder.getPizzaType(), savedOrder.getStatus());
    }

    public List<OrderResponseDTO> getAllOrders(OrderStatus orderStatus) {
        List<Order> orders = new ArrayList<>(Collections.emptyList());
        if (orderStatus == null) {
            orders.addAll(orderRepository.findAllByOrderByCreatedDate());
        } orders.addAll(orderRepository.findAllByStatusOrderByCreatedDate(orderStatus));
        return orders.stream().map(orderMapper::fromOrderToOrderResponseDTO).toList();
    }

    public OrderResponseDTO getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return new OrderResponseDTO(order.getId(), order.getCustomerName(), order.getPizzaType(), order.getStatus());
    }

    /**
     * Update order status
     * If status is PRONTA, set nextOrderId to the first order in IN_CODA status
     * If status change is not valid ({@link OrderStatus#isValidStatusChange(OrderStatus) see the logic here}), throw StatusChangeNotValidException
     * @param id     order id
     * @param status new status
     * @return {@link OrderResponseDTO} updated order
     */
    @Transactional
    public OrderResponseDTO updateOrderStatus(Long id, OrderStatus status) throws OrderNotFoundException, StatusChangeNotValidException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!order.getStatus().isValidStatusChange(status)) {
            throw new StatusChangeNotValidException("The order " + id + " status cannot be changed from " + order.getStatus() + " to " + status + " status.");
        }
        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);

        OrderResponseDTO response = new OrderResponseDTO(savedOrder.getId(), savedOrder.getCustomerName(), savedOrder.getPizzaType(), savedOrder.getStatus());
        if (status == OrderStatus.PRONTA) {
            orderRepository.findFirstByStatus(OrderStatus.IN_CODA).ifPresent((Order order1) -> response.setNextOrderId(order1.getId()));
        }
        return response;
    }
}

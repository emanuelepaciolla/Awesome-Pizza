package com.exercise.pizzeria.usecase;

import com.exercise.pizzeria.model.OrderRequestDTO;
import com.exercise.pizzeria.model.OrderResponseDTO;
import com.exercise.pizzeria.model.OrderStatus;
import com.exercise.pizzeria.model.PizzaType;
import com.exercise.pizzeria.entity.Order;
import com.exercise.pizzeria.exception.OrderNotFoundException;
import com.exercise.pizzeria.exception.StatusChangeNotValidException;
import com.exercise.pizzeria.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    public void testCreateOrder() {
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setCustomerName("Mario");
        requestDTO.setPizzaType(PizzaType.MARGHERITA);

        Order order = createOrder(1L, "Mario", PizzaType.MARGHERITA, OrderStatus.IN_CODA);

        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        OrderResponseDTO responseDTO = orderService.createOrder(requestDTO);

        assertEquals(1L, responseDTO.getId());
        assertEquals("Mario", responseDTO.getCustomerName());
        assertEquals(PizzaType.MARGHERITA, responseDTO.getPizzaType());
        assertEquals(OrderStatus.IN_CODA, responseDTO.getStatus());
    }

    @Test
    public void testGetAllOrdersRetrurnsAllOrdersWithoutFilter() {
        OrderResponseDTO order1 = new OrderResponseDTO();
        order1.setId(1L);
        order1.setCustomerName("Mario");
        order1.setPizzaType(PizzaType.MARGHERITA);
        order1.setStatus(OrderStatus.IN_CODA);

        OrderResponseDTO order2 = new OrderResponseDTO();
        order2.setId(2L);
        order2.setCustomerName("Luigi");
        order2.setPizzaType(PizzaType.CAPRICCIOSA);
        order2.setStatus(OrderStatus.PRONTA);

        List<OrderResponseDTO> orders = Arrays.asList(order1, order2);

        Mockito.when(orderRepository.findAllOrders()).thenReturn(orders);

        List<OrderResponseDTO> responseDTOs = orderService.getAllOrders(null);

        assertEquals(2, responseDTOs.size());
    }

    @Test
    public void testGetAllOrdersRetrurnsAllOrdersWithFilter() {
        OrderResponseDTO order1 = new OrderResponseDTO();
        order1.setId(1L);
        order1.setCustomerName("Mario");
        order1.setPizzaType(PizzaType.MARGHERITA);
        order1.setStatus(OrderStatus.IN_CODA);

        OrderResponseDTO order2 = new OrderResponseDTO();
        order2.setId(2L);
        order2.setCustomerName("Luigi");
        order2.setPizzaType(PizzaType.CAPRICCIOSA);
        order2.setStatus(OrderStatus.PRONTA);

        List<OrderResponseDTO> orders = List.of(order2);

        Mockito.when(orderRepository.findAllOrders(any())).thenReturn(orders);

        List<OrderResponseDTO> responseDTOs = orderService.getAllOrders(OrderStatus.PRONTA);

        verify(orderRepository, times(0)).findAllOrders();
        verify(orderRepository, times(1)).findAllOrders(OrderStatus.PRONTA);

        assertEquals(1, responseDTOs.size());
        assertEquals(2L, orders.get(0).getId());
        assertEquals(PizzaType.CAPRICCIOSA, orders.get(0).getPizzaType());
        assertEquals(OrderStatus.PRONTA, orders.get(0).getStatus());
        assertEquals("Luigi", orders.get(0).getCustomerName());
    }

    @Test
    public void testGetOrder() {
        Order order = createOrder(1L, "Mario", PizzaType.MARGHERITA, OrderStatus.IN_CODA);

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponseDTO responseDTO = orderService.getOrder(1L);

        assertEquals(1L, responseDTO.getId());
        assertEquals("Mario", responseDTO.getCustomerName());
        assertEquals(PizzaType.MARGHERITA, responseDTO.getPizzaType());
        assertEquals(OrderStatus.IN_CODA, responseDTO.getStatus());
    }

    @Test
    public void testGetOrderNotFound() {
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.getOrder(1L));
    }

    @Test
    public void testUpdateOrderStatusReturnCorrectOrderUpdated() throws OrderNotFoundException {
        Order order = createOrder(1L, "Mario", PizzaType.MARGHERITA, OrderStatus.IN_CODA);

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(createOrder(1L, "Mario", PizzaType.MARGHERITA, OrderStatus.IN_PREPARAZIONE));

        OrderResponseDTO responseDTO = orderService.updateOrderStatus(1L, OrderStatus.IN_PREPARAZIONE);
        verify(orderRepository, times(1)).save(order);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(0)).findFirstByStatus(OrderStatus.IN_CODA);

        assertEquals(OrderStatus.IN_PREPARAZIONE, responseDTO.getStatus());
        assertEquals(null, responseDTO.getNextOrderId());
    }

    @Test
    public void testUpdateOrderStatusReturnCorrectOrderUpdatedWithNextOrderIdIfStatusIsPronta() throws OrderNotFoundException {
        Order order = createOrder(1L, "Mario", PizzaType.MARGHERITA, OrderStatus.IN_PREPARAZIONE);
        Order order2 = createOrder(2L, "Luigi", PizzaType.CAPRICCIOSA, OrderStatus.IN_CODA);

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(createOrder(1L, "Mario", PizzaType.MARGHERITA, OrderStatus.PRONTA));
        Mockito.when(orderRepository.findFirstByStatus(OrderStatus.IN_CODA)).thenReturn(Optional.of(order2));

        OrderResponseDTO responseDTO = orderService.updateOrderStatus(1L, OrderStatus.PRONTA);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(order);
        verify(orderRepository, times(1)).findFirstByStatus(OrderStatus.IN_CODA);

        assertEquals(OrderStatus.PRONTA, responseDTO.getStatus());
        assertEquals(2L, responseDTO.getNextOrderId());
    }

    @Test
    public void testUpdateOrderStatusThrowsOrderNotFoundIfNoOrderIsPresent() throws OrderNotFoundException {
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.updateOrderStatus(1L, OrderStatus.IN_PREPARAZIONE));
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(0)).save(any());
        verify(orderRepository, times(0)).findFirstByStatus(any());

    }

    @Test
    public void testUpdateOrderStatusThrowsStatusNotValid() {
        Order order = createOrder(1L, "Mario", PizzaType.MARGHERITA, OrderStatus.IN_PREPARAZIONE);

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        StatusChangeNotValidException statusChangeNotValidException = assertThrows(StatusChangeNotValidException.class, () -> orderService.updateOrderStatus(1L, OrderStatus.IN_CODA));
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(0)).save(order);
        verify(orderRepository, times(0)).findFirstByStatus(OrderStatus.IN_CODA);

        assertEquals("The order 1 status cannot be changed from IN_PREPARAZIONE to IN_CODA status.", statusChangeNotValidException.getMessage());

    }

    private Order createOrder(Long id, String customerName, PizzaType pizzaType, OrderStatus status) {
        Order order = new Order();
        order.setId(id);
        order.setCustomerName(customerName);
        order.setPizzaType(pizzaType);
        order.setStatus(status);
        return order;
    }
}
package com.exercise.pizzeria.controller;

import com.exercise.pizzeria.model.OrderRequestDTO;
import com.exercise.pizzeria.model.OrderResponseDTO;
import com.exercise.pizzeria.model.OrderStatus;
import com.exercise.pizzeria.model.PizzaType;
import com.exercise.pizzeria.exception.ErrorMessage;
import com.exercise.pizzeria.usecase.OrderService;
import com.exercise.pizzeria.validation.PizzaTypeDeserializer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class,
        excludeAutoConfiguration = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(PizzaType.class, new PizzaTypeDeserializer());
        objectMapper.registerModule(module);
    }


    @Test
    public void testCreateOrder() throws Exception {
        OrderResponseDTO order = new OrderResponseDTO();
        order.setId(1L);
        order.setCustomerName("Mario");
        order.setPizzaType(PizzaType.MARGHERITA);
        order.setStatus(OrderStatus.IN_CODA);

        Mockito.when(orderService.createOrder(any(OrderRequestDTO.class))).thenReturn(order);

        MvcResult mvcResult = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new OrderRequestDTO().setPizzaType(PizzaType.MARGHERITA).setCustomerName("Mario"))))
                .andExpect(status().isCreated()).andReturn();

        OrderResponseDTO orderResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponseDTO.class);
        assertEquals(orderResponse.getCustomerName(), "Mario");

    }

    @Test
    public void testGetOrderStatus() throws Exception {
        OrderResponseDTO order = new OrderResponseDTO();
        order.setId(1L);
        order.setCustomerName("Mario");
        order.setPizzaType(PizzaType.MARGHERITA);
        order.setStatus(OrderStatus.IN_CODA);

        Mockito.when(orderService.createOrder(any())).thenReturn(order);

        MvcResult mvcResult = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new OrderRequestDTO().setPizzaType(PizzaType.MARGHERITA).setCustomerName("Mario"))))
                .andExpect(status().isCreated()).andReturn();

        OrderResponseDTO orderResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponseDTO.class);
        assertEquals(orderResponse.getCustomerName(), "Mario");

    }

    @Test
    public void testGetAllOrders() throws Exception {
        OrderResponseDTO order = new OrderResponseDTO();
        order.setId(1L);
        order.setCustomerName("Mario");
        order.setPizzaType(PizzaType.MARGHERITA);
        order.setStatus(OrderStatus.IN_CODA);

        OrderResponseDTO order2 = new OrderResponseDTO();
        order2.setId(2L);
        order2.setCustomerName("Flavio");
        order2.setPizzaType(PizzaType.CAPRICCIOSA);
        order2.setStatus(OrderStatus.IN_PREPARAZIONE);

        Mockito.when(orderService.getAllOrders(null)).thenReturn(List.of(order, order2));

        MvcResult mvcResult = mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new OrderRequestDTO().setPizzaType(PizzaType.MARGHERITA).setCustomerName("Mario"))))
                .andExpect(status().isOk()).andReturn();

        List<OrderResponseDTO> orderResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        assertEquals(orderResponse.size(), 2);
        Map<Long, OrderResponseDTO> ordersReponseMap = orderResponse.stream().collect(Collectors.toMap(OrderResponseDTO::getId, o -> o));
        assertTrue(ordersReponseMap.containsKey(1L));
        assertTrue(ordersReponseMap.containsKey(2L));
        OrderResponseDTO firstOrder = ordersReponseMap.get(1L);
        assertEquals(firstOrder.getCustomerName(), "Mario");
        assertEquals(firstOrder.getPizzaType(), PizzaType.MARGHERITA);
        assertEquals(firstOrder.getStatus(), OrderStatus.IN_CODA);
        OrderResponseDTO secondOrder = ordersReponseMap.get(2L);
        assertEquals(secondOrder.getCustomerName(), "Flavio");
        assertEquals(secondOrder.getPizzaType(), PizzaType.CAPRICCIOSA);
        assertEquals(secondOrder.getStatus(), OrderStatus.IN_PREPARAZIONE);
    }

    @Test
    public void testUpdateOrderStatus() throws Exception {
        OrderResponseDTO order = new OrderResponseDTO();
        order.setId(1L);
        order.setCustomerName("Mario");
        order.setPizzaType(PizzaType.MARGHERITA);
        order.setStatus(OrderStatus.IN_CODA);

        Mockito.when(orderService.updateOrderStatus(1L, OrderStatus.IN_PREPARAZIONE)).thenReturn(order);

        MvcResult mvcResult = mockMvc.perform(put("/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("status", OrderStatus.IN_PREPARAZIONE))))
                .andExpect(status().isOk()).andReturn();

        OrderResponseDTO orderResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponseDTO.class);
        assertEquals(orderResponse.getCustomerName(), "Mario");
    }

    @Test
    public void testCreateOrderFailsIfPizzaTypeIsNotCorrect() throws Exception {
        OrderResponseDTO order = new OrderResponseDTO();
        order.setId(1L);
        order.setCustomerName("Mario");
        order.setPizzaType(PizzaType.MARGHERITA);
        order.setStatus(OrderStatus.IN_CODA);

        Mockito.when(orderService.createOrder(any(OrderRequestDTO.class))).thenReturn(order);

        MvcResult mvcResult = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("pizzaType", "pizzaType", "customerName", "Mario"))))
                .andExpect(status().isBadRequest()).andReturn();

        ErrorMessage errorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorMessage.class);

        assertEquals(400, errorResponse.getCode());
        assertEquals("JSON parse error: Invalid pizza type: " + "PIZZATYPE" + ". Valid values are: " + Arrays.toString(PizzaType.values()), errorResponse.getDescription());
    }
}

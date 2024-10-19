package com.exercise.pizzeria.repository;

import com.exercise.pizzeria.model.OrderResponseDTO;
import com.exercise.pizzeria.model.OrderStatus;
import com.exercise.pizzeria.entity.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Sql("/data.sql")
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    static void h2Properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:testdb");
        registry.add("spring.datasource.driverClassName", () -> "org.h2.Driver");
        registry.add("spring.datasource.username", () -> "sa");
        registry.add("spring.datasource.password", () -> "");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.H2Dialect");
    }

    @AfterEach()
    public void cleanUp() {
        orderRepository.deleteAll();
    }

    @Test
    public void findFirstByStatusReturn() {
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name, pizza_type, status) " +
                "VALUES (1, 'Mario', 'MARGHERITA', 'PRONTA')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name, pizza_type, status) " +
                "VALUES (2, 'Francesca', 'MARGHERITA', 'IN_CODA')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name,  pizza_type, status) " +
                "VALUES (3, 'Matteo', 'MARGHERITA', 'IN_CODA')");


        Optional<Order> order = orderRepository.findFirstByStatus(OrderStatus.IN_CODA);
        assertTrue(order.isPresent());
        Order dbOrder = order.get();
        assertEquals(2, dbOrder.getId());
    }

    @Test
    public void findFirstByStatusReturnEmptyValueIfNotMatchingStatusIsPresent() {
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name, pizza_type, status) " +
                "VALUES (1, 'Mario', 'MARGHERITA', 'PRONTA')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name, pizza_type, status) " +
                "VALUES (2, 'Francesca', 'MARGHERITA', 'IN_PREPARAZIONE')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name,  pizza_type, status) " +
                "VALUES (3, 'Matteo', 'MARGHERITA', 'IN_PREPARAZIONE')");


        Optional<Order> order = orderRepository.findFirstByStatus(OrderStatus.IN_CODA);
        assertTrue(order.isEmpty());
    }


    @Test
    public void findAllOrdersReturnEmptyValueIfNotOrderIsPresent() {
        List<OrderResponseDTO> order = orderRepository.findAllOrders();
        assertTrue(order.isEmpty());
    }

    @Test
    public void findAllOrdersReturnAllValues() {
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name, pizza_type, status) " +
                "VALUES (1, 'Mario', 'MARGHERITA', 'PRONTA')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name, pizza_type, status) " +
                "VALUES (2, 'Francesca', 'MARGHERITA', 'IN_PREPARAZIONE')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name,  pizza_type, status) " +
                "VALUES (3, 'Matteo', 'MARGHERITA', 'IN_PREPARAZIONE')");
        List<OrderResponseDTO> order = orderRepository.findAllOrders();
        assertEquals(3, order.size());
    }

    @Test
    public void findAllOrdersReturnAllValuesWithCorrectStatus() {
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name, pizza_type, status) " +
                "VALUES (1, 'Mario', 'MARGHERITA', 'PRONTA')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name, pizza_type, status) " +
                "VALUES (2, 'Francesca', 'MARGHERITA', 'IN_PREPARAZIONE')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name,  pizza_type, status) " +
                "VALUES (3, 'Matteo', 'MARGHERITA', 'IN_CODA')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name,  pizza_type, status) " +
                "VALUES (4, 'Roberta', 'DIAVOLA', 'IN_CODA')");

        List<OrderResponseDTO> order = orderRepository.findAllOrders(OrderStatus.IN_CODA);
        assertEquals(2, order.size());
        assertTrue(order.stream().allMatch(o -> o.getStatus().equals(OrderStatus.IN_CODA)));
        List<Long> list = order.stream().map(OrderResponseDTO::getId).toList();
        assertTrue(list.containsAll(List.of(3L, 4L)));
    }

    @Test
    public void findAllOrdersShouldNotReturnAllValuesWithCorrectStatus() {
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name, pizza_type, status) " +
                "VALUES (1, 'Mario', 'MARGHERITA', 'PRONTA')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name, pizza_type, status) " +
                "VALUES (2, 'Francesca', 'MARGHERITA', 'IN_CODA')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name,  pizza_type, status) " +
                "VALUES (3, 'Matteo', 'MARGHERITA', 'IN_CODA')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name,  pizza_type, status) " +
                "VALUES (4, 'Roberta', 'DIAVOLA', 'IN_CODA')");

        List<OrderResponseDTO> order = orderRepository.findAllOrders(OrderStatus.IN_PREPARAZIONE);
        assertEquals(0, order.size());
    }

    @Test
    public void findAllOrdersShouldReturnAllOrdersOrderedByCreationDate() {
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name, pizza_type, status, created_date) " +
                "VALUES (1, 'Mario', 'MARGHERITA', 'PRONTA', '2021-01-01 00:00:00')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name, pizza_type, status, created_date) " +
                "VALUES (2, 'Francesca', 'MARGHERITA', 'IN_CODA', '2021-02-01 00:00:00')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name,  pizza_type, status, created_date) " +
                "VALUES (3, 'Matteo', 'MARGHERITA', 'IN_CODA', '2021-01-11 00:00:00')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name,  pizza_type, status, created_date) " +
                "VALUES (4, 'Roberta', 'DIAVOLA', 'IN_CODA', '2021-01-11 00:00:03')");

        List<OrderResponseDTO> order = orderRepository.findAllOrders();
        assertEquals(4, order.size());
        assertEquals(1L, order.get(0).getId());
        assertEquals(3L, order.get(1).getId());
        assertEquals(4L, order.get(2).getId());
        assertEquals(2L, order.get(3).getId());

    }

    @Test
    public void findAllOrdersWithStatusShouldReturnAllOrdersOrderedByCreationDate() {
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name, pizza_type, status, created_date) " +
                "VALUES (1, 'Mario', 'MARGHERITA', 'PRONTA', '2021-01-01 00:00:00')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name, pizza_type, status, created_date) " +
                "VALUES (2, 'Francesca', 'MARGHERITA', 'IN_CODA', '2021-02-01 00:00:00')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name,  pizza_type, status, created_date) " +
                "VALUES (3, 'Matteo', 'MARGHERITA', 'IN_CODA', '2021-01-11 00:00:00')");
        jdbcTemplate.update("INSERT INTO ordine (id,customer_name,  pizza_type, status, created_date) " +
                "VALUES (4, 'Roberta', 'DIAVOLA', 'IN_CODA', '2021-01-11 00:00:03')");

        List<OrderResponseDTO> order = orderRepository.findAllOrders(OrderStatus.IN_CODA);
        assertEquals(3, order.size());
        assertEquals(3L, order.get(0).getId());
        assertEquals(4L, order.get(1).getId());
        assertEquals(2L, order.get(2).getId());

    }
}
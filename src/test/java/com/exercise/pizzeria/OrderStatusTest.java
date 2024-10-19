package com.exercise.pizzeria;

import com.exercise.pizzeria.model.OrderStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderStatusTest {

    @Test
    public void testIsValidStatusChangeFromInCodaToInPreparazione() {
        OrderStatus status = OrderStatus.IN_CODA;
        assertTrue(status.isValidStatusChange(OrderStatus.IN_PREPARAZIONE));
    }

    @Test
    public void testIsValidStatusChangeFromInPreparazioneToPronta() {
        OrderStatus status = OrderStatus.IN_PREPARAZIONE;
        assertTrue(status.isValidStatusChange(OrderStatus.PRONTA));
    }

    @Test
    public void testIsValidStatusChangeFromInCodaToPronta() {
        OrderStatus status = OrderStatus.IN_CODA;
        assertFalse(status.isValidStatusChange(OrderStatus.PRONTA));
    }

    @Test
    public void testIsValidStatusChangeFromProntaToInPreparazione() {
        OrderStatus status = OrderStatus.PRONTA;
        assertFalse(status.isValidStatusChange(OrderStatus.IN_PREPARAZIONE));
    }

    @Test
    public void testIsValidStatusChangeFromProntaToInCoda() {
        OrderStatus status = OrderStatus.PRONTA;
        assertFalse(status.isValidStatusChange(OrderStatus.IN_CODA));
    }
}
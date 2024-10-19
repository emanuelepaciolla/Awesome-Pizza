package com.exercise.pizzeria.validation;

import com.exercise.pizzeria.exception.EnumNotValidException;
import com.exercise.pizzeria.model.OrderStatus;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderStatusDeserializerTest {

    private OrderStatusDeserializer deserializer;
    private JsonParser jsonParserMock;
    private DeserializationContext deserializationContextMock;

    @BeforeEach
    public void setup() {
        deserializer = new OrderStatusDeserializer();
        jsonParserMock = Mockito.mock(JsonParser.class);
        deserializationContextMock = Mockito.mock(DeserializationContext.class);
    }

    @Test
    public void testDeserializeValidOrderStatus() throws IOException {
        Mockito.when(jsonParserMock.getText()).thenReturn("IN_CODA");
        OrderStatus result = deserializer.deserialize(jsonParserMock, deserializationContextMock);

        assertEquals(OrderStatus.IN_CODA, result);
    }

    @Test
    public void testDeserializeValidOrderStatusCaseInsensitive() throws IOException {
        Mockito.when(jsonParserMock.getText()).thenReturn("in_coda");
        OrderStatus result = deserializer.deserialize(jsonParserMock, deserializationContextMock);

        assertEquals(OrderStatus.IN_CODA, result);
    }

    @Test
    public void testDeserializeInvalidOrderStatusThrowsException() throws IOException {
        Mockito.when(jsonParserMock.getText()).thenReturn("Error");
        EnumNotValidException exception = assertThrows(EnumNotValidException.class,
                () -> deserializer.deserialize(jsonParserMock, deserializationContextMock));

        assertEquals("Invalid orderStatus type: ERROR. Valid values are: " + Arrays.toString(OrderStatus.values()), exception.getMessage());
    }
}
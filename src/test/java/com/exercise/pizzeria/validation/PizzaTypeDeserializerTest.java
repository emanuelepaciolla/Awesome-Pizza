package com.exercise.pizzeria.validation;

import com.exercise.pizzeria.exception.EnumNotValidException;
import com.exercise.pizzeria.model.PizzaType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PizzaTypeDeserializerTest {

    private PizzaTypeDeserializer deserializer;
    private JsonParser jsonParserMock;
    private DeserializationContext deserializationContextMock;

    @BeforeEach
    public void setup() {
        deserializer = new PizzaTypeDeserializer();
        jsonParserMock = Mockito.mock(JsonParser.class);
        deserializationContextMock = Mockito.mock(DeserializationContext.class);
    }

    @Test
    public void testDeserializeValidPizzaType() throws IOException {
        Mockito.when(jsonParserMock.getText()).thenReturn("MARGHERITA");
        PizzaType result = deserializer.deserialize(jsonParserMock, deserializationContextMock);

        assertEquals(PizzaType.MARGHERITA, result);
    }

    @Test
    public void testDeserializeValidPizzaTypeCaseInsensitive() throws IOException {
        Mockito.when(jsonParserMock.getText()).thenReturn("margherita");
        PizzaType result = deserializer.deserialize(jsonParserMock, deserializationContextMock);

        assertEquals(PizzaType.MARGHERITA, result);
    }

    @Test
    public void testDeserializeInvalidPizzaTypeThrowsException() throws IOException {
        Mockito.when(jsonParserMock.getText()).thenReturn("FUNGHI_E_SALSICCIA");
        EnumNotValidException exception = assertThrows(EnumNotValidException.class,
                () -> deserializer.deserialize(jsonParserMock, deserializationContextMock));

        assertEquals("Invalid pizza type: FUNGHI_E_SALSICCIA. Valid values are: " + Arrays.toString(PizzaType.values()), exception.getMessage());
    }
}

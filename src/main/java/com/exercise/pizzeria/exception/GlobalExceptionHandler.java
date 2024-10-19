package com.exercise.pizzeria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({EnumNotValidException.class, OrderNotFoundException.class, StatusChangeNotValidException.class})
    public ResponseEntity<ErrorMessage> enumNotValidExceptionHandling(RuntimeException ex) {
        return new ResponseEntity<>(new ErrorMessage(400, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessage> handleEnumConversionException(MethodArgumentTypeMismatchException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setCode(400);
        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            String enumType = ex.getRequiredType().getSimpleName();
            String message = String.format("Invalid value '%s' for enum '%s'. Supported values are: %s",
                    ex.getValue(),
                    enumType,
                    String.join(", ", getEnumValues(ex.getRequiredType())));
            errorMessage.setDescription(message);
        } else {
            errorMessage.setDescription(ex.getMessage());
        }

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    private String[] getEnumValues(Class<?> enumType) {
        return Arrays.stream(enumType.getEnumConstants())
                .map(Object::toString)
                .toArray(String[]::new);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorMessage> errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        new ErrorMessage()
                                .setDescription(error.getField() + ": " + error.getDefaultMessage())
                                .setCode(400))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(ex.getStatusCode().value(), errorMessages);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}

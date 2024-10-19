package com.exercise.pizzeria.exception;

import java.util.List;

public class ErrorResponse {

    private Integer code;
    private List<ErrorMessage> errorMessages;


    public ErrorResponse(Integer code, List<ErrorMessage> errorMessages) {
        this.code = code;
        this.errorMessages = errorMessages;
    }

    public ErrorResponse() {
    }

    public List<ErrorMessage> getErrorMessages() {
        return errorMessages;
    }

    public Integer getCode() {
        return code;
    }
}
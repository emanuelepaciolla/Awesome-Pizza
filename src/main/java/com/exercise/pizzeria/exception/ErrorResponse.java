package com.exercise.pizzeria.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "400")
    private Integer code;
    @Schema(description = "List of error messages")
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
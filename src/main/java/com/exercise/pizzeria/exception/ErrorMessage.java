package com.exercise.pizzeria.exception;

import io.swagger.v3.oas.annotations.media.Schema;

public class ErrorMessage {
    @Schema(description = "Error code", example = "400")
    private Integer code;
    @Schema(description = "Error description", example = "Bad Request")
    private String description;

    public ErrorMessage(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public ErrorMessage() {
    }

    public Integer getCode() {
        return code;
    }

    public ErrorMessage setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ErrorMessage setDescription(String description) {
        this.description = description;
        return this;
    }
}

package com.exercise.pizzeria.exception;

public class ErrorMessage {
    private Integer code;
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

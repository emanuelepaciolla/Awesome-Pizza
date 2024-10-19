package com.exercise.pizzeria.exception;

public class EnumNotValidException extends RuntimeException{

    public EnumNotValidException(String message) {
        super(message);
    }

}

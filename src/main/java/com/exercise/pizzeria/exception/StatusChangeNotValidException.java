package com.exercise.pizzeria.exception;

public class StatusChangeNotValidException extends RuntimeException{

    public StatusChangeNotValidException(String message) {
        super(message);
    }
}

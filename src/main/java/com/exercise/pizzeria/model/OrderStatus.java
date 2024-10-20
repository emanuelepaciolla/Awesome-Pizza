package com.exercise.pizzeria.model;

public enum OrderStatus {
    IN_CODA, IN_PREPARAZIONE, PRONTA;

    /**
     * Check if the status change is valid
     * IN_CODA -> IN_PREPARAZIONE
     * IN_PREPARAZIONE -> PRONTA
     * else false
     *
     * @param newStatus
     * @return
     */
    public boolean isValidStatusChange(OrderStatus newStatus) {
        return switch (this) {
            case IN_CODA -> newStatus == IN_PREPARAZIONE;
            case IN_PREPARAZIONE -> newStatus == PRONTA;
            default -> false;
        };

    }
}

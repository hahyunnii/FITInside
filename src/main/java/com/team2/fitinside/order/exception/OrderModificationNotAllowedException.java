package com.team2.fitinside.order.exception;

public class OrderModificationNotAllowedException extends RuntimeException {
    public OrderModificationNotAllowedException(String message) {
        super(message);
    }
}

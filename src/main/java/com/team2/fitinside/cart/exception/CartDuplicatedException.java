package com.team2.fitinside.cart.exception;

public class CartDuplicatedException extends RuntimeException {
    public CartDuplicatedException(String message) {
        super(message);
    }
}

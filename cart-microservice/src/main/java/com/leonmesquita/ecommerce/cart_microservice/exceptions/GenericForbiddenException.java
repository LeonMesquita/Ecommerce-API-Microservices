package com.leonmesquita.ecommerce.cart_microservice.exceptions;

public class GenericForbiddenException extends RuntimeException {
    public GenericForbiddenException(String message) {
        super(message);
    }
}

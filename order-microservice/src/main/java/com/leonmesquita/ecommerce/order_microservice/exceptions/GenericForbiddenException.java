package com.leonmesquita.ecommerce.order_microservice.exceptions;

public class GenericForbiddenException extends RuntimeException {
    public GenericForbiddenException(String message) {
        super(message);
    }
}

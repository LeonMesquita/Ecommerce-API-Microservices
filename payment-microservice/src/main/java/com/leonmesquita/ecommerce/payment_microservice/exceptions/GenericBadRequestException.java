package com.leonmesquita.ecommerce.payment_microservice.exceptions;

public class GenericBadRequestException extends RuntimeException {
    public GenericBadRequestException(String message) {
        super(message);
    }
}

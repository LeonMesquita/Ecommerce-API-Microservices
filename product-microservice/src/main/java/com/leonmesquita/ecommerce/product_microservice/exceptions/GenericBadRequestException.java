package com.leonmesquita.ecommerce.product_microservice.exceptions;

public class GenericBadRequestException extends RuntimeException {
    public GenericBadRequestException(String message) {
        super(message);
    }
}
package com.leonmesquita.ecommerce.product_microservice.exceptions;

public class GenericNotFoundException extends RuntimeException {
    public GenericNotFoundException(String message) {
        super(message);
    }
}

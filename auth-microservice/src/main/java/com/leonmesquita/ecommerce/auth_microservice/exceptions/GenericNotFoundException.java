package com.leonmesquita.ecommerce.auth_microservice.exceptions;

public class GenericNotFoundException extends RuntimeException {
    public GenericNotFoundException(String message) {
        super(message);
    }
}

package com.leonmesquita.ecommerce.cart_microservice.exceptions;

public class GenericConflictException extends RuntimeException{
    public GenericConflictException(String message) {
        super(message);
    }
}
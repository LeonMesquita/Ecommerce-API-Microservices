package com.leonmesquita.ecommerce.order_microservice.exceptions;

public class GenericConflictException extends RuntimeException{
    public GenericConflictException(String message) {
        super(message);
    }
}
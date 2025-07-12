package com.leonmesquita.ecommerce.payment_microservice.exceptions;

public class GenericConflictException extends RuntimeException{
    public GenericConflictException(String message) {
        super(message);
    }
}
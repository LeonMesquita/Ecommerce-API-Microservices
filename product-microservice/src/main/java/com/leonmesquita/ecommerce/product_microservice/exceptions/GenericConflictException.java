package com.leonmesquita.ecommerce.product_microservice.exceptions;

public class GenericConflictException extends RuntimeException{
    public GenericConflictException(String message) {
        super(message);
    }
}
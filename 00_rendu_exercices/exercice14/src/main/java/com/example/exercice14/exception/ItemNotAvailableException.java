package com.example.exercice14.exception;

public class ItemNotAvailableException extends RuntimeException {

    public ItemNotAvailableException(String itemId) {
        super("Item is not available: " + itemId);
    }
}

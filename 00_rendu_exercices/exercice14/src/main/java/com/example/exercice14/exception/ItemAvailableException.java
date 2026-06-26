package com.example.exercice14.exception;

public class ItemAvailableException extends RuntimeException {

    public ItemAvailableException(String itemId) {
        super("Item is available, borrow it instead: " + itemId);
    }
}

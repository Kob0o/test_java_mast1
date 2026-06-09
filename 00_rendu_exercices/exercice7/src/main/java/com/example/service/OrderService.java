package com.example.service;

import com.example.model.CustomerProfile;
import com.example.model.Order;
import com.example.model.OrderResult;
import com.example.repository.ProductRepository;

public class OrderService {

    private final ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public OrderResult placeOrder(Order order, CustomerProfile profile) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}

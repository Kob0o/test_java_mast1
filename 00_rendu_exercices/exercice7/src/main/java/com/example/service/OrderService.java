package com.example.service;

import com.example.model.CustomerProfile;
import com.example.model.Order;
import com.example.model.OrderReceipt;
import com.example.model.OrderResult;
import com.example.model.Product;
import com.example.repository.ProductRepository;

import java.util.Optional;

public class OrderService {

    private final ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public OrderResult placeOrder(Order order, CustomerProfile profile) {
        Optional<Product> productOptional = productRepository.findByReference(order.getProductReference());

        if (productOptional.isEmpty()) {
            return new OrderResult(false, null);
        }

        Product product = productOptional.get();

        if (order.getQuantity() > product.getStock()) {
            return new OrderResult(false, null);
        }

        double subtotal = product.getUnitPrice() * order.getQuantity();
        double totalAmount = subtotal * (1 - getDiscountRate(profile));

        OrderReceipt receipt = new OrderReceipt(
                order.getProductReference(),
                order.getQuantity(),
                totalAmount,
                "Commande confirmée"
        );

        return new OrderResult(true, receipt);
    }

    private double getDiscountRate(CustomerProfile profile) {
        return switch (profile) {
            case STANDARD -> 0.0;
            case PREMIUM -> 0.10;
            case VIP -> 0.20;
        };
    }
}

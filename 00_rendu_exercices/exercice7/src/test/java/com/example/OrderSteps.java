package com.example;

import com.example.model.CustomerProfile;
import com.example.model.Order;
import com.example.model.OrderResult;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.service.OrderService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderSteps {

    private ProductRepository productRepository;
    private OrderService orderService;
    private String customerEmail;
    private CustomerProfile customerProfile;
    private OrderResult orderResult;

    @Given("a product {string} named {string} with unit price {double} and stock {int}")
    public void aProductNamedWithUnitPriceAndStock(String reference, String name, double unitPrice, int stock) {
        productRepository = mock(ProductRepository.class);
        orderService = new OrderService(productRepository);

        Product product = new Product(reference, name, unitPrice, stock);
        when(productRepository.findByReference(reference)).thenReturn(Optional.of(product));
    }

    @Given("no product exists with reference {string}")
    public void noProductExistsWithReference(String reference) {
        productRepository = mock(ProductRepository.class);
        orderService = new OrderService(productRepository);

        when(productRepository.findByReference(reference)).thenReturn(Optional.empty());
    }

    @Given("a customer with email {string} and profile {word}")
    public void aCustomerWithEmailAndProfile(String email, String profile) {
        customerEmail = email;
        customerProfile = CustomerProfile.valueOf(profile);
    }

    @When("I place an order for {int} units of {string}")
    public void iPlaceAnOrderForUnitsOf(int quantity, String productReference) {
        placeOrder(quantity, productReference);
    }

    @When("I place an order for {int} unit of {string}")
    public void iPlaceAnOrderForUnitOf(int quantity, String productReference) {
        placeOrder(quantity, productReference);
    }

    private void placeOrder(int quantity, String productReference) {
        Order order = new Order(customerEmail, productReference, quantity);
        orderResult = orderService.placeOrder(order, customerProfile);
    }

    @Then("the order should be accepted")
    public void theOrderShouldBeAccepted() {
        assertNotNull(orderResult);
        assertTrue(orderResult.isAccepted());
        assertNotNull(orderResult.getReceipt());
    }

    @Then("the order should be refused")
    public void theOrderShouldBeRefused() {
        assertNotNull(orderResult);
        assertFalse(orderResult.isAccepted());
        assertNull(orderResult.getReceipt());
    }

    @Then("the receipt product reference should be {string}")
    public void theReceiptProductReferenceShouldBe(String expectedReference) {
        assertEquals(expectedReference, orderResult.getReceipt().getProductReference());
    }

    @Then("the receipt quantity should be {int}")
    public void theReceiptQuantityShouldBe(int expectedQuantity) {
        assertEquals(expectedQuantity, orderResult.getReceipt().getQuantity());
    }

    @Then("the receipt total amount should be {double}")
    public void theReceiptTotalAmountShouldBe(double expectedTotalAmount) {
        assertEquals(expectedTotalAmount, orderResult.getReceipt().getTotalAmount());
    }

    @Then("the receipt should have a confirmation message")
    public void theReceiptShouldHaveAConfirmationMessage() {
        String confirmationMessage = orderResult.getReceipt().getConfirmationMessage();
        assertNotNull(confirmationMessage);
        assertFalse(confirmationMessage.isBlank());
    }

    @Then("the product repository should have been queried for {string}")
    public void theProductRepositoryShouldHaveBeenQueriedFor(String reference) {
        verify(productRepository).findByReference(reference);
    }
}

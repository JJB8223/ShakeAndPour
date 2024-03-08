package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Provides JUnit test cases for the ShoppingCart class to verify correct behavior 
 * of adding and removing products, calculating total cost, and clearing the cart.
 * 
 * @author David Dobbins dpd8504
 */
public class ShoppingCartTest {
    private ShoppingCart cart;
    private Kit kit1;
    private Kit kit2;

    /**
     * Sets up the test environment before each test method.
     * Initializes a new ShoppingCart and two products, milk and cola, to be used in the tests.
     */
    @BeforeEach 
    public void constructorSetUp() {
        cart = new ShoppingCart();
        ArrayList<Integer> products = new ArrayList<>();
        products.add(1);
        products.add(2);
        products.add(3);
        ArrayList<Integer> products2 = new ArrayList<>();
        products2.add(4);
        products2.add(5);
        Product milk = new Product(1, "Milk", 2.99f, 50);
        Product rose = new Product(3, "Rose Water", 4.00f, 50);
        Product vodka = new Product(4, "Vodka", 5.00f, 40);
        Product orange = new Product(5, "Orange", 2.10f, 60);
        Product cola = new Product(2, "Cola", 1.99f, 50);
        kit1 = new Kit(1, "First", 15.00f, 50, products);
        kit2 = new Kit(2, "Second", 20.00f, 25, products);
    }

    /**
     * Tests adding products to and removing products from the shopping cart.
     * Verifies that the quantities in the cart are updated correctly after these operations.
     */
    @Test
    public void testAddAndRemoveProduct() {
        cart.addProduct(milk, 2);
        cart.addProduct(cola, 3);
        cart.removeProduct(cola, 1);
        assertEquals(2, cart.getProducts().get(milk).intValue(), "Soda quantity should be 2");
        assertEquals(2, cart.getProducts().get(cola).intValue(), "Tea quantity after removal should be 2");
    }

    /**
     * Tests the calculation of the total cost of products in the shopping cart.
     * Verifies that the total cost is correctly calculated based on the products' prices and quantities.
     */
    @Test
    public void testGetTotalCost() {
        cart.addProduct(milk, 2);
        cart.addProduct(cola, 3);
        assertEquals(11.95f, cart.getTotalCost(), 0.001, "Total cost should reflect sum of drink costs");
    }

    /**
     * Tests clearing all products from the shopping cart.
     * Verifies that the cart is empty after the clear operation.
     */
    @Test
    public void testClearCart() {
        cart.addProduct(milk, 3);
        cart.addProduct(cola, 5);
        cart.clearCart();
        assertTrue(cart.getProducts().isEmpty(), "Cart should be completely cleared");
    }
}

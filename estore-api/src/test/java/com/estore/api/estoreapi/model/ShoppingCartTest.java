package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShoppingCartTest {
    private ShoppingCart cart;
    private Product milk;
    private Product cola;

    @BeforeEach 
    public void constructorSetUp() {
        cart = new ShoppingCart();
        milk = new Product(1, "Milk", 2.99f, 50);
        cola = new Product(2, "Cola", 1.99f, 50);
    }

    @Test
    public void testAddAndRemoveProduct() {
        cart.addProduct(milk, 2);
        cart.addProduct(cola, 3);
        cart.removeProduct(cola, 1);
        assertEquals(2, cart.getDrinks().get(milk).intValue(), "Soda quantity should be 2");
        assertEquals(2, cart.getDrinks().get(cola).intValue(), "Tea quantity after removal should be 2");
    }

    @Test
    public void testGetTotalCost() {
        cart.addProduct(milk, 2);
        cart.addProduct(cola, 3);
        assertEquals(11.95f, cart.getTotalCost(), 0.001, "Total cost should reflect sum of drink costs");
    }

    @Test
    public void testClearCart() {
        cart.addProduct(milk, 3);
        cart.addProduct(cola, 5);
        cart.clearCart();
        assertTrue(cart.getDrinks().isEmpty(), "Cart should be completely cleared");
    }
}

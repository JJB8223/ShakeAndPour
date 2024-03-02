package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.persistence.InventoryDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

@Tag("Controller-tier")
public class ShoppingCartControllerTest {

    private ShoppingCartController shoppingCartController;
    private InventoryDAO mockInventoryDAO;

    @BeforeEach
    public void setup() {
        mockInventoryDAO = mock(InventoryDAO.class);
        shoppingCartController = new ShoppingCartController(mockInventoryDAO);
    }

    @Test
    public void testAddToCart() throws IOException {
        int productId = 1;
        int quantity = 5;
        Product product = new Product(productId, "Soda", 2.99f, 20);
        when(mockInventoryDAO.getProduct(productId)).thenReturn(product);

        ResponseEntity<Void> response = shoppingCartController.addtoCart(productId, quantity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddToCartProductNotFound() throws IOException {
        int productId = 1;
        int quantity = 5;
        when(mockInventoryDAO.getProduct(productId)).thenReturn(null);

        ResponseEntity<Void> response = shoppingCartController.addtoCart(productId, quantity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddToCartInsufficientQuantity() throws IOException {
        int productId = 1;
        int quantity = 25;
        Product product = new Product(productId, "Soda", 2.99f, 20);
        when(mockInventoryDAO.getProduct(productId)).thenReturn(product);

        ResponseEntity<Void> response = shoppingCartController.addtoCart(productId, quantity);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAddToCartHandleException() throws IOException {
        int productId = 1;
        int quantity = 5;
        doThrow(new IOException()).when(mockInventoryDAO).getProduct(productId);

        ResponseEntity<Void> response = shoppingCartController.addtoCart(productId, quantity);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCart() throws IOException {
        int productId = 1;
        int quantity = 5;
        Product product = new Product(productId, "Soda", 2.99f, 20);
        when(mockInventoryDAO.getProduct(productId)).thenReturn(product);
        shoppingCartController.addtoCart(productId, quantity); // Add product to cart before attempting to remove

        ResponseEntity<Void> response = shoppingCartController.removeFromCart(productId, quantity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCartProductNotFound() throws IOException {
        int productId = 1;
        int quantity = 5;

        when(mockInventoryDAO.getProduct(productId)).thenReturn(null);

        ResponseEntity<Void> response = shoppingCartController.removeFromCart(productId, quantity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCartHandleException() throws IOException {
        int productId = 1;
        int quantity = 5;
        doThrow(new IOException()).when(mockInventoryDAO).getProduct(productId);

        ResponseEntity<Void> response = shoppingCartController.removeFromCart(productId, quantity);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetCartProducts() throws IOException {
        int productId1 = 1, productId2 = 2;
        Product milk = new Product(productId1, "Milk", 2.99f, 50);
        Product cola = new Product(productId2, "Cola", 1.99f, 50);
        when(mockInventoryDAO.getProduct(productId1)).thenReturn(milk);
        when(mockInventoryDAO.getProduct(productId2)).thenReturn(cola);
        shoppingCartController.addtoCart(productId1, 2); // Add products to the cart
        shoppingCartController.addtoCart(productId2, 3);

        ResponseEntity<Map<Product, Integer>> response = shoppingCartController.getCartProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().get(milk).intValue());
        assertEquals(3, response.getBody().get(cola).intValue());
    }

    @Test
    public void testGetCartProductsWhenEmpty() throws IOException {
        ResponseEntity<Map<Product, Integer>> response = shoppingCartController.getCartProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }
}


package com.estore.api.estoreapi.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.estore.api.estoreapi.persistence.InventoryDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

/**
 * Provides JUnit test cases for the ShoppingCartController class to verify the correct handling
 * of adding and removing products from a shopping cart. It tests various scenarios including
 * successful operations, handling non-existent products, handling insufficient quantities,
 * and dealing with internal server errors.
 *
 * @see ShoppingCartController
 * 
 * @author David Dobbins dpd8504
 */

@Tag("Controller-tier")
public class ShoppingCartControllerTest {

    private ShoppingCartController shoppingCartController;
    private InventoryDAO mockInventoryDAO;
    private ShoppingCart mockShoppingCart;

    /**
     * Sets up the test environment before each test method. This includes initializing a mock
     * InventoryDAO, a mock ShoppingCart, and the ShoppingCartController with these mocks.
     */
    @BeforeEach
    public void setup() {
        mockInventoryDAO = mock(InventoryDAO.class);
        mockShoppingCart = mock(ShoppingCart.class);
        shoppingCartController = new ShoppingCartController(mockInventoryDAO, mockShoppingCart);
    }

    /**
     * Tests adding a product to the shopping cart when the product exists and there is sufficient
     * quantity available. Expects the operation to succeed with an HTTP status of OK (200).
     *
     * @throws IOException if an I/O error occurs which is simulated for testing error handling.
     */
    @Test
    public void testAddToCart() throws IOException {
        int productId = 1;
        int quantity = 5;
        Product product = new Product(productId, "Soda", 2.99f, 20);
        when(mockInventoryDAO.getProduct(productId)).thenReturn(product);

        ResponseEntity<Void> response = shoppingCartController.addtoCart(productId, quantity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Tests adding a product to the shopping cart when the product does not exist in the inventory.
     * Expects the operation to fail with an HTTP status of NOT_FOUND (404).
     *
     * @throws IOException if an I/O error occurs which is simulated for testing error handling.
     */
    @Test
    public void testAddToCartProductNotFound() throws IOException {
        int productId = 1;
        int quantity = 5;
        when(mockInventoryDAO.getProduct(productId)).thenReturn(null);

        ResponseEntity<Void> response = shoppingCartController.addtoCart(productId, quantity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Tests adding a product to the shopping cart when the requested quantity exceeds the available
     * stock. Expects the operation to fail with an HTTP status of BAD_REQUEST (400).
     *
     * @throws IOException if an I/O error occurs which is simulated for testing error handling.
     */
    @Test
    public void testAddToCartInsufficientQuantity() throws IOException {
        int productId = 1;
        int quantity = 25;
        Product product = new Product(productId, "Soda", 2.99f, 20);
        when(mockInventoryDAO.getProduct(productId)).thenReturn(product);

        ResponseEntity<Void> response = shoppingCartController.addtoCart(productId, quantity);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Tests adding a product to the shopping cart when an internal error occurs (e.g., database
     * connection issue). Expects the operation to fail with an HTTP status of INTERNAL_SERVER_ERROR (500).
     *
     * @throws IOException if an I/O error occurs, simulating an internal server error.
     */
    @Test
    public void testAddToCartHandleException() throws IOException {
        int productId = 1;
        int quantity = 5;
        doThrow(new IOException()).when(mockInventoryDAO).getProduct(productId);

        ResponseEntity<Void> response = shoppingCartController.addtoCart(productId, quantity);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * Tests removing a product from the shopping cart when the product exists and the quantity to
     * remove is valid. Expects the operation to succeed with an HTTP status of OK (200).
     *
     * @throws IOException if an I/O error occurs which is simulated for testing error handling.
     */
    @Test
    public void testRemoveFromCart() throws IOException {
        int productId = 1;
        int quantity = 5;
        Product product = new Product(productId, "Soda", 2.99f, 20);
        when(mockInventoryDAO.getProduct(productId)).thenReturn(product);
        when(mockShoppingCart.containsProduct(product)).thenReturn(true);
        when(mockShoppingCart.getProductQuantity(product)).thenReturn(quantity);

        ResponseEntity<Void> response = shoppingCartController.removeFromCart(productId, quantity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Tests removing a product from the shopping cart that does not exist in the cart. Expects the
     * operation to fail with an HTTP status of NOT_FOUND (404).
     *
     * @throws IOException if an I/O error occurs which is simulated for testing error handling.
     */
    @Test
    public void testRemoveFromCartProductNotFound() throws IOException {
        int productId = 1;
        int quantity = 5;
        Product product = new Product(productId, "Soda", 2.99f, 20);
        when(mockInventoryDAO.getProduct(productId)).thenReturn(product);
        when(mockShoppingCart.containsProduct(product)).thenReturn(false);

        ResponseEntity<Void> response = shoppingCartController.removeFromCart(productId, quantity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Tests removing a product from the shopping cart when an internal error occurs (e.g., database
     * connection issue). Expects the operation to fail with an HTTP status of INTERNAL_SERVER_ERROR (500).
     *
     * @throws IOException if an I/O error occurs, simulating an internal server error.
     */
    @Test
    public void testRemoveFromCartHandleException() throws IOException {
        int productId = 1;
        int quantity = 5;
        doThrow(new IOException()).when(mockInventoryDAO).getProduct(productId);

        ResponseEntity<Void> response = shoppingCartController.removeFromCart(productId, quantity);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
    * Tests retrieving all products from the shopping cart. 
    * Verifies that the correct products and quantities are returned and that the operation 
    * succeeds with an HTTP status of OK (200).
    *
    * @throws IOException if an I/O error occurs which is simulated for testing error handling.
    */
    @Test
    public void testGetCartProducts() throws IOException {
        Product milk = new Product(1, "Milk", 2.99f, 50);
        Product cola = new Product(2, "Cola", 1.99f, 50);
        Map<Product, Integer> expectedCartItems = Map.of(milk, 2, cola, 3);
        when(mockShoppingCart.getDrinks()).thenReturn(expectedCartItems);
        ResponseEntity<Map<Product, Integer>> response = shoppingCartController.getCartProducts();
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertEquals(2, response.getBody().get(milk).intValue(), "Milk quantity should be 2 in the cart");
        assertEquals(3, response.getBody().get(cola).intValue(), "Cola quantity should be 3 in the cart");
        assertEquals(expectedCartItems, response.getBody(), "Returned cart items should match expected items");
    }

    @Test
    public void testGetCartProductsWhenEmpty() throws IOException {
        when(mockShoppingCart.getDrinks()).thenReturn(Map.of()); // Return an empty map

        ResponseEntity<Map<Product, Integer>> response = shoppingCartController.getCartProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK for an empty cart");
        assertTrue(response.getBody().isEmpty(), "Cart should be empty");
    }
}

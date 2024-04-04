package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.estore.api.estoreapi.model.Kit;
import com.estore.api.estoreapi.model.ShoppingCartKit;
import com.estore.api.estoreapi.persistence.KitDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;

@Tag("Controller-tier")
public class ShoppingCartControllerTest {

    private ShoppingCartController shoppingCartController;
    private KitDAO mockKitDAO;


    @BeforeEach
    public void setup() {
        mockKitDAO = mock(KitDAO.class);
        shoppingCartController = new ShoppingCartController(mockKitDAO);
    }

    @Test
    public void testAddToCart() throws IOException {
        int userId = 123; // Example user ID
        int kitId = 1;
        int quantity = 5;
        ArrayList<Integer> products = new ArrayList<>();
        products.add(1);
        products.add(2);
        products.add(3);
        
        Kit kit1 = new Kit(kitId, "Soda", 2.99f, 20, products);
        when(mockKitDAO.getKit(kitId)).thenReturn(kit1);

        ResponseEntity<Void> response = shoppingCartController.addToCart(userId, kitId, quantity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddToCartKitNotFound() throws IOException {
        int userId = 123;
        int kitId = 1;
        int quantity = 5;
        when(mockKitDAO.getKit(kitId)).thenReturn(null);

        ResponseEntity<Void> response = shoppingCartController.addToCart(userId, kitId, quantity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddToCartInsufficientQuantity() throws IOException {
        int userId = 123;
        int kitId = 1;
        int quantity = 25;
        ArrayList<Integer> products = new ArrayList<Integer>();
        products.add(1);
        products.add(2);
        products.add(3);
        Kit kit = new Kit(kitId, "Soda", 2.99f, 20, products);
        when(mockKitDAO.getKit(kitId)).thenReturn(kit);

        ResponseEntity<Void> response = shoppingCartController.addToCart(userId, kitId, quantity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddToCartNoQuantity() throws IOException {
        int userId = 123;
        int kitId = 1;
        int quantity = 5;
        ArrayList<Integer> products = new ArrayList<Integer>();
        products.add(1);
        products.add(2);
        products.add(3);
        Kit kit = new Kit(kitId, "Soda", 2.99f, 0, products);
        when(mockKitDAO.getKit(kitId)).thenReturn(kit);

        ResponseEntity<Void> response = shoppingCartController.addToCart(userId, kitId, quantity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddToCartHandleException() throws IOException {
        int userId = 123;
        int kitId = 1;
        int quantity = 5;
        doThrow(new IOException()).when(mockKitDAO).getKit(kitId);

        ResponseEntity<Void> response = shoppingCartController.addToCart(userId, kitId, quantity);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCart() throws IOException {
        int userId = 123; // Example user ID
        int kitId = 1;
        int quantity = 5;
        ArrayList<Integer> products = new ArrayList<>();
        products.add(1);
        products.add(2);
        products.add(3);
        
        Kit kit = new Kit(kitId, "Soda", 2.99f, 20, products);
        when(mockKitDAO.getKit(kitId)).thenReturn(kit);
        // Assuming addtoCart is successful and the kit is added
        shoppingCartController.addToCart(userId, kitId, quantity);

        ResponseEntity<Void> response = shoppingCartController.removeFromCart(userId, kitId, quantity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCartKitNotFound() throws IOException {
        int userId = 123;
        int kitId = 1;
        int quantity = 5;

        when(mockKitDAO.getKit(kitId)).thenReturn(null);

        ResponseEntity<Void> response = shoppingCartController.removeFromCart(userId, kitId, quantity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCartGreaterQuantity() throws IOException {
        int kitId = 1;
        int quantity = 25;
        int userId = 123;
        ArrayList<Integer> products = new ArrayList<Integer>();
        products.add(1);
        products.add(2);
        products.add(3);
        Kit kit = new Kit(kitId, "Soda", 2.99f, 20, products);
        when(mockKitDAO.getKit(kitId)).thenReturn(kit);
        shoppingCartController.addToCart(userId, kitId, quantity); // Add kit to cart before attempting to remove

        ResponseEntity<Void> response = shoppingCartController.removeFromCart(userId, kitId, quantity);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCartHandleException() throws IOException {
        int kitId = 1;
        int quantity = 5;
        int userId = 123;
        doThrow(new IOException()).when(mockKitDAO).getKit(kitId);

        ResponseEntity<Void> response = shoppingCartController.removeFromCart(userId, kitId, quantity);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetCartKits() throws IOException {
        int kitId1 = 1, kitId2 = 2;
        int userId = 123;
        ArrayList<Integer> products = new ArrayList<>();
        ArrayList<Integer> products2 = new ArrayList<>();
        products.add(1);
        products.add(2);
        products.add(3);
        products2.add(4);
        products2.add(5);
        Kit kit1 = new Kit(kitId1, "Milk", 2.99f, 50, products);
        Kit kit2 = new Kit(kitId2, "Cola", 1.99f, 50, products2);
        when(mockKitDAO.getKit(kitId1)).thenReturn(kit1);
        when(mockKitDAO.getKit(kitId2)).thenReturn(kit2);
        shoppingCartController.addToCart(userId, kitId1, 3); // Add kits to the cart
        shoppingCartController.addToCart(userId, kitId2, 4);

        ResponseEntity<ArrayList<ShoppingCartKit>> response = shoppingCartController.getCartKits(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetCartKitsWhenEmpty() throws IOException {
        int userId = 123;
        ResponseEntity<ArrayList<ShoppingCartKit>> response = shoppingCartController.getCartKits(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void testGetFullCartKits() throws IOException{
        int kitId1 = 1, kitId2 = 2;
        int userId = 123;
        ArrayList<Integer> products = new ArrayList<Integer>();
        ArrayList<Integer> products2 = new ArrayList<Integer>();
        products.add(1);
        products.add(2);
        products.add(3);
        products2.add(4);
        products2.add(5);
        Kit kit1 = new Kit(kitId1, "Milk", 2.99f, 50, products);
        Kit kit2 = new Kit(kitId2, "Cola", 1.99f, 50, products2);
        when(mockKitDAO.getKit(kitId1)).thenReturn(kit1);
        when(mockKitDAO.getKit(kitId2)).thenReturn(kit2);
        shoppingCartController.addToCart(userId, kitId1, 2); // Add kits to the cart
        shoppingCartController.addToCart(userId, kitId2, 3);

        ResponseEntity<ArrayList<Kit>> response = shoppingCartController.getFullCartKits(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());

    }

    @Test
    public void testGetFullCartKitsEmpty() throws IOException{
        int userId = 123;
        ResponseEntity<ArrayList<Kit>> response = shoppingCartController.getFullCartKits(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }


    @Test
    public void testGetTotalCost_EmptyCart() throws IOException {
        int userId = 123;
        ResponseEntity<Float> response = shoppingCartController.getTotalCost(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0.0f, response.getBody());
    }

    @Test
    public void testGetTotalCost() throws IOException {
        int kitId1 = 1, kitId2 = 2;
        int userId = 123;
        ArrayList<Integer> products = new ArrayList<Integer>();
        ArrayList<Integer> products2 = new ArrayList<Integer>();
        products.add(1);
        products.add(2);
        products.add(3);
        products2.add(4);
        products2.add(5);
        Kit kit1 = new Kit(kitId1, "Milk", 2.99f, 50, products);
        Kit kit2 = new Kit(kitId2, "Cola", 1.99f, 50, products2);
        when(mockKitDAO.getKit(kitId1)).thenReturn(kit1);
        when(mockKitDAO.getKit(kitId2)).thenReturn(kit2);
        shoppingCartController.addToCart(userId, kitId1, 2); // Add kits to the cart
        shoppingCartController.addToCart(userId, kitId2, 3);

        ResponseEntity<Float> response = shoppingCartController.getTotalCost(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(11.95f, response.getBody());
    }

    @Test
    public void testClearCart() throws IOException {
        int kitId1 = 1, kitId2 = 2;
        int userId = 123;
        ArrayList<Integer> products = new ArrayList<Integer>();
        ArrayList<Integer> products2 = new ArrayList<Integer>();
        products.add(1);
        products.add(2);
        products.add(3);
        products2.add(4);
        products2.add(5);
        Kit kit1 = new Kit(kitId1, "Milk", 2.99f, 50, products);
        Kit kit2 = new Kit(kitId2, "Cola", 1.99f, 50, products2);
        when(mockKitDAO.getKit(kitId1)).thenReturn(kit1);
        when(mockKitDAO.getKit(kitId2)).thenReturn(kit2);
        shoppingCartController.addToCart(userId, kitId1, 2); // Add kits to the cart
        shoppingCartController.addToCart(userId, kitId2, 3);

        ResponseEntity<Void> response = shoppingCartController.clearCart(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}


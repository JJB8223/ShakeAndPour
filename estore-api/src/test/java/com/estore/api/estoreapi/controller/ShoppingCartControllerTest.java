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
import java.util.Map;

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
        int kitId = 1;
        int quantity = 5;
        ArrayList<Integer> products = new ArrayList<Integer>();
        products.add(1);
        products.add(2);
        products.add(3);
        
        Kit kit1 = new Kit(kitId, "Soda", 2.99f, 20, products);
        when(mockKitDAO.getKit(kitId)).thenReturn(kit1);

        ResponseEntity<Void> response = shoppingCartController.addtoCart(kitId, quantity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddToCartKitNotFound() throws IOException {
        int kitId = 1;
        int quantity = 5;
        when(mockKitDAO.getKit(kitId)).thenReturn(null);

        ResponseEntity<Void> response = shoppingCartController.addtoCart(kitId, quantity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddToCartInsufficientQuantity() throws IOException {
        int kitId = 1;
        int quantity = 25;
        ArrayList<Integer> products = new ArrayList<Integer>();
        products.add(1);
        products.add(2);
        products.add(3);
        Kit kit = new Kit(kitId, "Soda", 2.99f, 20, products);
        when(mockKitDAO.getKit(kitId)).thenReturn(kit);

        ResponseEntity<Void> response = shoppingCartController.addtoCart(kitId, quantity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddToCartNoQuantity() throws IOException {
        int kitId = 1;
        int quantity = 0;
        ArrayList<Integer> products = new ArrayList<Integer>();
        products.add(1);
        products.add(2);
        products.add(3);
        Kit kit = new Kit(kitId, "Soda", 2.99f, 0, products);
        when(mockKitDAO.getKit(kitId)).thenReturn(kit);

        ResponseEntity<Void> response = shoppingCartController.addtoCart(kitId, quantity);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAddToCartHandleException() throws IOException {
        int kitId = 1;
        int quantity = 5;
        doThrow(new IOException()).when(mockKitDAO).getKit(kitId);

        ResponseEntity<Void> response = shoppingCartController.addtoCart(kitId, quantity);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCart() throws IOException {
        int kitId = 1;
        int quantity = 5;
        ArrayList<Integer> products = new ArrayList<Integer>();
        products.add(1);
        products.add(2);
        products.add(3);
        Kit kit = new Kit(kitId, "Soda", 2.99f, 20, products);
        when(mockKitDAO.getKit(kitId)).thenReturn(kit);
        shoppingCartController.addtoCart(kitId, quantity); // Add kit to cart before attempting to remove

        ResponseEntity<Void> response = shoppingCartController.removeFromCart(kitId, quantity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCartKitNotFound() throws IOException {
        int kitId = 1;
        int quantity = 5;

        when(mockKitDAO.getKit(kitId)).thenReturn(null);

        ResponseEntity<Void> response = shoppingCartController.removeFromCart(kitId, quantity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCartGreaterQuantity() throws IOException {
        int kitId = 1;
        int quantity = 25;
        ArrayList<Integer> products = new ArrayList<Integer>();
        products.add(1);
        products.add(2);
        products.add(3);
        Kit kit = new Kit(kitId, "Soda", 2.99f, 20, products);
        when(mockKitDAO.getKit(kitId)).thenReturn(kit);
        shoppingCartController.addtoCart(kitId, quantity); // Add kit to cart before attempting to remove

        ResponseEntity<Void> response = shoppingCartController.removeFromCart(kitId, quantity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCartHandleException() throws IOException {
        int kitId = 1;
        int quantity = 5;
        doThrow(new IOException()).when(mockKitDAO).getKit(kitId);

        ResponseEntity<Void> response = shoppingCartController.removeFromCart(kitId, quantity);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /*
    @Test
    public void testGetCartKits() throws IOException {
        int kitId1 = 1, kitId2 = 2;
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
        shoppingCartController.addtoCart(kitId1, 2); // Add kits to the cart
        shoppingCartController.addtoCart(kitId2, 3);

        ResponseEntity<ArrayList<ShoppingCartKit>> response = shoppingCartController.getCartKits();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().get(0).getQuantity());
        assertEquals(3, response.getBody().get(1).getQuantity());
    }
    */

    @Test
    public void testGetCartKitsWhenEmpty() throws IOException {
        ResponseEntity<ArrayList<ShoppingCartKit>> response = shoppingCartController.getCartKits();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void testGetTotalCost_EmptyCart() throws IOException {
        ResponseEntity<Float> response = shoppingCartController.getTotalCost();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0.0f, response.getBody());
    }

    @Test
    public void testGetTotalCost() throws IOException {
        int kitId1 = 1, kitId2 = 2;
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
        shoppingCartController.addtoCart(kitId1, 2); // Add kits to the cart
        shoppingCartController.addtoCart(kitId2, 3);

        ResponseEntity<Float> response = shoppingCartController.getTotalCost();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(11.95f, response.getBody());
    }
}


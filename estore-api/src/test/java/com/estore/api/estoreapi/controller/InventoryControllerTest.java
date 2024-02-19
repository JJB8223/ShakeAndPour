package com.estore.api.estoreapi.controller;


import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.persistence.InventoryDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test the Inventory Controller class and its methods
 *
 * @author Matthew Morrison msm8275
 */
@Tag("Controller-tier")
public class InventoryControllerTest {
    private InventoryController inventoryController;
    private InventoryDAO mockInventoryDAO;

    /**
     * Before each test, create a new InventoryController object
     * and inject a mock Inventory DAO
     */
    @BeforeEach
    public void setupInventoryController(){
        mockInventoryDAO = mock(InventoryDAO.class);
        inventoryController = new InventoryController((mockInventoryDAO));
    }

    @Test
    public void testCreateProduct() throws IOException{
        // Setup
        Product p = new Product(99, "Soda", 2.99f, 20);
        // when createInventory is called, return true simulating successful
        // creation and save
        when(mockInventoryDAO.createProduct(p)).thenReturn(p);

        // invoke
        ResponseEntity<Product> response = inventoryController.createProduct(p);

        // analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(p,response.getBody());
    }

    @Test
    public void testCreateProductFail() throws IOException{
        // Setup
        Product p = new Product(99, "Soda", 2.99f, 20);
        // when createInventory is called, return false simulating failed
        // creation and save
        when(mockInventoryDAO.createProduct(p)).thenReturn(null);

        // invoke
        ResponseEntity<Product> response = inventoryController.createProduct(p);

        // analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testGetProducts() throws IOException{
        // Setup
        Product[] products = new Product[2];
        products[0] = new Product(99, "Soda", 2.99f, 20);
        products[1] = new Product(98, "Milk", 3.99f, 50);
        // When getProducts is called return the heroes created above
        when(mockInventoryDAO.getProducts()).thenReturn(products);

        // invoke
        ResponseEntity<Product[]> response = inventoryController.getProducts();

        // analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(p,response.getBody());
    }

}

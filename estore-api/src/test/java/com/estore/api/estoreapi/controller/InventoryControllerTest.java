package com.estore.api.estoreapi.controller;


import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.persistence.InventoryDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test the Inventory Controller class and its methods
 *
 * @author Matthew Morrison msm8275
 * @author Joshua Bay jjb8223
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
    public void testCreateProductException() throws IOException{
        Product p = new Product(99, "Soda", 2.99f, 20);
        doThrow(new IOException()).when(mockInventoryDAO).createProduct(p);

        // Invoke
        ResponseEntity<Product> response = inventoryController.createProduct(p);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());


    }

    @Test
    public void testProductSearch() throws IOException {
        // Setup
        String searchString = "la";
        Product[] products = new Product[2];
        products[0] = new Product(99,"Mineral Water",3.40f,25);
        products[1] = new Product(98,"Rose Water",3.40f,25);
        // When findProducsts is called with the search string, return the two
        /// products above
        when(mockInventoryDAO.findProducts(searchString)).thenReturn(products);
        
        // Invoke
        ResponseEntity<Product[]> response = inventoryController.searchProducts(searchString);
        
        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(products, response.getBody());
    }

    @Test
    public void testProductSearchFail() throws IOException{
        // Setup
        String searchString = "an";
        // When createProduct is called on the Mock product DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).findProducts(searchString);

        // Invoke
        ResponseEntity<Product[]> response = inventoryController.searchProducts(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
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
        // When getProducts is called return the products!!!!!!!!!!!!!!!!!!!!!!!!!!!!! created above
        when(mockInventoryDAO.getProducts()).thenReturn(products);

        // invoke
        ResponseEntity<Product[]> response = inventoryController.getProducts();

        // analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(products,response.getBody());
    }


    @Test
    public void testGetProductsHandleException() throws IOException { // getProducts may throw IOException
        // Setup
        // When getProducts is called on the Mock InventoryDAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).getProducts();

        // Invoke
        ResponseEntity<Product[]> response = inventoryController.getProducts();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetProduct() throws IOException {  // getProduct may throw IOException
        // Setup
        Product product = new Product(42, "MilkShake", 7.99f, 12);
        // When the same id is passed in, our mock Product DAO will return the Product object
        when(mockInventoryDAO.getProduct(product.getId())).thenReturn(product);

        // Invoke
        ResponseEntity<Product> response = inventoryController.getProduct(product.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(product,response.getBody());
    }

    @Test
    public void testGetProductNotFound() throws Exception { // createProduct may throw IOException
        // Setup
        int productId = 42;
        // When the same id is passed in, our mock Product DAO will return null, simulating
        // no Product found
        when(mockInventoryDAO.getProduct(productId)).thenReturn(null);

        // Invoke
        ResponseEntity<Product> response = inventoryController.getProduct(productId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetProductHandleException() throws IOException {  // getProduct may throw IOException
        // Setup
        int productId = 42;
        // When getProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).getProduct(productId);

        // Invoke
        ResponseEntity<Product> response = inventoryController.getProduct(productId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteProduct() throws IOException { // deleteProduct may throw IOException
        // Setup
        int productId = 99;
        // when deleteProduct is called return true, simulating successful deletion
        when(mockInventoryDAO.deleteProduct(productId)).thenReturn(true);

        // Invoke
        ResponseEntity<Product> response = inventoryController.deleteProduct(productId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteProductNotFound() throws IOException { // deleteProduct may throw IOException
        // Setup
        int productId = 99;
        // when deleteProduct is called return false, simulating failed deletion
        when(mockInventoryDAO.deleteProduct(productId)).thenReturn(false);

        // Invoke
        ResponseEntity<Product> response = inventoryController.deleteProduct(productId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteProductHandleException() throws IOException { // deleteProduct may throw IOException
        // Setup
        int productId = 99;
        // When deleteProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockInventoryDAO).deleteProduct(productId);

        // Invoke
        ResponseEntity<Product> response = inventoryController.deleteProduct(productId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }


    @Test
    public void testUpdateProductSuccess() throws IOException {
        // add initial product
        Product p = new Product(99, "Soda", 2.99f, 20);

        when(mockInventoryDAO.createProduct(p)).thenReturn(p);

        // invoke
        ResponseEntity<Product> response = inventoryController.createProduct(p);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());

        // now update product
        p = new Product(99, "Ramune Soda", 2.99f, 40);
        when(mockInventoryDAO.updateProduct(p)).thenReturn(p);
        ResponseEntity<Product> status = inventoryController.updateProduct(p);

        assertEquals(HttpStatus.OK,status.getStatusCode());

    }

    @Test
    public void testUpdateProductNotFound() throws IOException {
        // add initial product
        Product p = new Product(99, "Soda", 2.99f, 20);

        when(mockInventoryDAO.createProduct(p)).thenReturn(p);

        // invoke
        ResponseEntity<Product> response = inventoryController.createProduct(p);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());

        // try to update product not in inventory
        p = new Product(100, "Ramune Soda", 2.99f, 40);
        when(mockInventoryDAO.updateProduct(p)).thenReturn(null);
        ResponseEntity<Product> status = inventoryController.updateProduct(p);

        assertEquals(HttpStatus.NOT_FOUND,status.getStatusCode());

    }

    @Test
    public void testUpdateProductException() throws IOException {
        // add initial product
        Product p = new Product(99, "Soda", 2.99f, 20);

        when(mockInventoryDAO.createProduct(p)).thenReturn(p);

        // invoke
        ResponseEntity<Product> response = inventoryController.createProduct(p);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());

        // try to update product not in inventory
        p = new Product(100, "Ramune Soda", 2.99f, 40);
        doThrow(new IOException()).when(mockInventoryDAO).updateProduct(p);
        ResponseEntity<Product> status = inventoryController.updateProduct(p);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,status.getStatusCode());

    }
}

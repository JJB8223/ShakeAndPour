package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test the Inventory File DAO class
 *
 * @author Matthew Morrison msm8275
 */
@Tag("Persistence-tier")
public class InventoryFileDAOTest {
    InventoryFileDAO inventoryFileDAO;
    Product[] testInventory;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setupInventoryFileDAO() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        testInventory = new Product[3];
        testInventory[0] = new Product(99, "Soda", 1.99F, 20);
        testInventory[1] = new Product(100, "Water", 0.99F, 200);
        testInventory[2] = new Product(101, "Ramune", 3.99F, 10);
        when(mockObjectMapper
                .readValue(new File("doesnt_matter.txt"),Product[].class))
                .thenReturn(testInventory);
        inventoryFileDAO = new InventoryFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetInventory() {
        Product[] inv = inventoryFileDAO.getProducts();

        assertEquals(inv.length, testInventory.length);
        for(int i = 0; i < testInventory.length; i++) {
            assertEquals(inv[i], testInventory[i]);
        }
    }

    @Test
    public void testFindProducts(){
        Product[] inv = inventoryFileDAO.findProducts("da");

        assertEquals(inv.length, 1);
        assertEquals(inv[0], testInventory[0]);

        inv = inventoryFileDAO.findProducts("ne");

        assertEquals(inv.length, 1);
        assertEquals(inv[0], testInventory[2]);
    }

    @Test
    public void testGetProduct(){
        Product p = inventoryFileDAO.getProduct(99);

        assertEquals(p, testInventory[0]);

        p = inventoryFileDAO.getProduct(100);

        assertEquals(p, testInventory[1]);
    }

    @Test
    public void testDeleteProd(){
        boolean r = assertDoesNotThrow(() -> inventoryFileDAO.deleteProduct(99),
                "Unexpected exception thrown");
        assertTrue(r);
        assertEquals(inventoryFileDAO.inventory.size(), testInventory.length-1);
    }

    @Test
    public void testCreateProd(){
        Product p = new Product(102, "Sparkling Water", 3.00F, 10);
        Product r = assertDoesNotThrow(() -> inventoryFileDAO.createProduct(p),
                "Unexpected exception thrown");

        assertNotNull(r);
        Product actual = inventoryFileDAO.getProduct(p.getId());
        assertEquals(actual.getId(), p.getId());
        assertEquals(actual.getName(), p.getName());
        assertEquals(actual.getQuantity(), p.getQuantity());
        assertEquals(actual.getPrice(), p.getPrice());

    }


}

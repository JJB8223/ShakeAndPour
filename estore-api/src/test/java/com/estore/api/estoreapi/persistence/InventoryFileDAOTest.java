package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;

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


}

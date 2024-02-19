package com.estore.api.estoreapi.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit testing for the Product class
 *
 * @author Matthew Morrison msm8275
 */
@Tag("Model-tier")
public class ProductTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        String expected_name = "Soda";
        float expected_price = 2.99f;
        int expected_quantity = 25;

        Product p = new Product(expected_id, expected_name,
                expected_price, expected_quantity);

        // Analyze
        assertEquals(expected_id,p.getId());
        assertEquals(expected_name,p.getName());
        assertEquals(expected_price,p.getPrice());
        assertEquals(expected_quantity,p.getQuantity());
    }

    @Test
    public void testName() {
        // Setup
        int id = 99;
        String name = "Soda";
        float price = 2.99f;
        int quantity = 25;
        Product p = new Product(id, name,
                price, quantity);

        String expected_name = "Pepsi";
        // Invoke
        p.setName(expected_name);
        // Analyze
        assertEquals(expected_name,p.getName());
    }

    @Test
    public void testPrice() {
        // Setup
        int id = 99;
        String name = "Soda";
        float price = 2.99f;
        int quantity = 25;
        Product p = new Product(id, name,
                price, quantity);

        float expected_price = 1.99f;
        // Invoke
        p.setPrice(expected_price);
        // Analyze
        assertEquals(expected_price, p.getPrice());

    }

    @Test
    public void testQuantity() {
        // Setup
        int id = 99;
        String name = "Soda";
        float price = 2.99f;
        int quantity = 25;
        Product p = new Product(id, name,
                price, quantity);

        int expected_quantity = 10;
        // Invoke
        p.setQuantity(expected_quantity);
        // Analyze
        assertEquals(expected_quantity, p.getQuantity());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String name = "Soda";
        float price = 2.99f;
        int quantity = 25;
        String expected_string = String.format(Product.STRING_FORMAT,
                id, name, price, quantity);

        Product p = new Product(id, name,
                price, quantity);

        // Invoke
        String actual_string = p.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }

}

package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;



@Tag("Model-tier")
public class KitTest {

    @Test
    public void testCtor() {
        // Setup values
        int expected_id = 99;
        String expected_name = "Test Kit";
        float expected_price = 30.99f;
        int expected_quant = 35;
        // setting up the products list
        ArrayList<Integer> expected_products = new ArrayList<>();
        expected_products.add(1);
        expected_products.add(2);
        expected_products.add(3);

        Kit k = new Kit(expected_id, expected_name, expected_price, expected_quant, expected_products);

        assertEquals(expected_id, k.getId());
        assertEquals(expected_name, k.getName());
        assertEquals(expected_price, k.getPrice());
        assertEquals(expected_quant,k.getQuantity());
        assertEquals(expected_products, k.getProductsInKit());
        
    }

    @Test
    public void testName() {
        // Setup values
        int expected_id = 99;
        String expected_name = "Test Kit";
        float expected_price = 30.99f;
        int expected_quant = 35;
        // setting up the products list
        ArrayList<Integer> expected_products = new ArrayList<>();
        expected_products.add(1);
        expected_products.add(2);
        expected_products.add(3);

        Kit k = new Kit(expected_id, expected_name, expected_price, expected_quant, expected_products);

        String changed_name = "Changed Test Name";

        k.setName(changed_name);

        assertEquals(changed_name, k.getName());

    }

    @Test
    public void testPrice(){
        // Setup values
        int expected_id = 99;
        String expected_name = "Test Kit";
        float expected_price = 30.99f;
        int expected_quant = 35;
        // setting up the products list
        ArrayList<Integer> expected_products = new ArrayList<>();
        expected_products.add(1);
        expected_products.add(2);
        expected_products.add(3);

        Kit k = new Kit(expected_id, expected_name, expected_price, expected_quant, expected_products);
    
        float changed_price = 41.99f;

        k.setPrice(changed_price);

        assertEquals(changed_price, k.getPrice());
    }

    @Test
    public void testQuantity() {
        // Setup values
        int expected_id = 99;
        String expected_name = "Test Kit";
        float expected_price = 30.99f;
        int expected_quant = 35;
        // setting up the products list
        ArrayList<Integer> expected_products = new ArrayList<>();
        expected_products.add(1);
        expected_products.add(2);
        expected_products.add(3);

        Kit k = new Kit(expected_id, expected_name, expected_price, expected_quant, expected_products);

        int changed_quantity = 12;

        k.setQuantity(changed_quantity);

        assertEquals(changed_quantity, k.getQuantity());
    }

    @Test
    public void testToString() {
        // Setup values
        int expected_id = 99;
        String expected_name = "Test Kit";
        float expected_price = 30.99f;
        int expected_quant = 35;
        // setting up the products list
        ArrayList<Integer> expected_products = new ArrayList<>();
        expected_products.add(1);
        expected_products.add(2);
        expected_products.add(3);

        Kit k = new Kit(expected_id, expected_name, expected_price, expected_quant, expected_products);
    
        String expected_string = Integer.toString(expected_id) + expected_name + String.valueOf(expected_price) + expected_products.toString();

        assertEquals(expected_string, k.toString());

    }


}

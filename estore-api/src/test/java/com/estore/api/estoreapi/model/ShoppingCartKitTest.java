package com.estore.api.estoreapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test the Shopping Cart Kit Controller class and its methods
 *
 * @author Matthew Morrison msm8275
 */
@Tag("Controller-tier")
public class ShoppingCartKitTest {
    private ShoppingCartKit kit1;

    int kitID = 1;
    int quantity = 10;
    String name = "test kit";
    float price = 10.00f;


    @BeforeEach
    public void setupShoppingCartKit() {
        kit1 = new ShoppingCartKit(kitID, quantity, name, price);
    }

    @Test
    public void testNewShoppingCartKit(){
        int kitID = 2;
        int quantity = 20;
        String name = "my kit";
        float price = 15.00f;
        ShoppingCartKit newKit = new ShoppingCartKit(kitID, quantity, name, price);
        assertEquals(newKit.getID(), kitID);
        assertEquals(newKit.getQuantity(), quantity);
        assertEquals(newKit.getPrice(), price);
        assertEquals(newKit.getName(), name);
    }

    @Test
    public void testGetID() {
        assertEquals(kit1.getID(), kitID);
    }

    @Test
    public void testGetQuantity() {
        assertEquals(kit1.getQuantity(), quantity);
    }

    @Test
    public void testGetName() {
        assertEquals(kit1.getName(), name);
    }

    @Test
    public void testGetPrice(){
        assertEquals(kit1.getPrice(), price);
    }

    @Test
    public void testTotalPrice() {
        float total = kit1.getQuantity() * kit1.getPrice();

        assertEquals(kit1.totalPrice(), total);
    }

}

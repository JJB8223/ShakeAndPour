package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Order class
 * 
 * @author Duncan French
 */
@Tag("Model-tier")
public class OrderTest {

    private Order testOrder;
    private String testUser;
    private Map<Kit, Integer> testMap;
    private int testId;
    private Kit firstTestKit;
    private Kit secondTestKit;
    private Kit thirdTestKit;

    /**
     * Sets up the testing environment before each method
     */
    @BeforeEach
    public void setup() {
        testUser = "test";
        testMap = new HashMap<>();
        firstTestKit = new Kit(1, "abc", 5.0f, 10, new ArrayList<Integer>());
        secondTestKit = new Kit(2, "def", 5.0f, 10, new ArrayList<Integer>());
        thirdTestKit = new Kit(3, "ghi", 5.0f, 10, new ArrayList<Integer>());
        testMap.put(firstTestKit, 3);
        testMap.put(secondTestKit, 5);
        testMap.put(thirdTestKit, 7);
        testId = 1;
        testOrder = new Order(testId, testUser, testMap);
    }

    /**
     * This method tests the getAllKits method in the Order class
     */
    @Test
    public void testGetAllKits() {
        // these should be equivalent since the order class doesn't create a new map, it just uses the one provided
        assertEquals(testOrder.getAllKits(), testMap); 
    }

    /**
     * This method tests the purchasedBy method in the Order class
     */
    @Test
    public void testPurchasedBy() {
        // the purchaser's username for the testOrder is in the testUser string
        assertTrue(testOrder.purchasedBy(testUser)); 
        // adding to the string should force purchasedBy to return false
        assertFalse(testOrder.purchasedBy(testUser + "fail"));
    }

    /**
     * This method tests the getKitQuantity in the Order class
     */
    @Test
    public void testGetKitQuantity() {
        // All of these quantities are assigned in the setup method
        assertEquals(testOrder.getKitQuantity(firstTestKit), 3);
        assertEquals(testOrder.getKitQuantity(secondTestKit), 5);
        assertEquals(testOrder.getKitQuantity(thirdTestKit), 7);
    }

    /**
     * This method tests the clearKitFromOrder method in the Order class
     */
    @Test
    public void testClearKitFromOrder() {
        Kit clearedKit = firstTestKit;
        testOrder.clearKitFromOrder(clearedKit); 
        // once we've cleared the kit from the order, it should return a quantity of 0
        assertEquals(testOrder.getKitQuantity(clearedKit), 0);
    }

    /**
     * This method tests the removeKits method
     */
    @Test
    public void testRemoveKits() {
        // testing for removing less than the number of kits currently in the order
        testOrder.removeKits(firstTestKit, 2);
        assertEquals(testOrder.getKitQuantity(firstTestKit), 1); 
       
        // testing for removing the number of kits currently in the order
        testOrder.removeKits(secondTestKit, 5);
        assertEquals(testOrder.getKitQuantity(secondTestKit), 0);
        Map<Kit, Integer> resultingMap = testOrder.getAllKits();
        assertEquals(resultingMap.get(secondTestKit), null); // verifying that the kit was actually removed from the map
        
        // testing for removing more than the number of kits currently in the order
        testOrder.removeKits(thirdTestKit, 10);
        assertEquals(testOrder.getKitQuantity(thirdTestKit), 0);
        resultingMap = testOrder.getAllKits();
        assertEquals(resultingMap.get(thirdTestKit), null); // verifying that the kit was actually removed from the map
    }

    /**
     * This method tests the addKits method in the Order class
     */
    @Test
    public void testAddKits() {
        // testing for adding a kit already in the map with a valid quantity
        testOrder.addKits(firstTestKit, 5);
        assertEquals(testOrder.getKitQuantity(firstTestKit), 8);

        // testing for adding a kit not in the map with a valid quantity
        Kit fourthTestKit = new Kit(4, "4", 4.0f, 4, new ArrayList<Integer>());
        testOrder.addKits(fourthTestKit, 4);
        assertEquals(testOrder.getKitQuantity(fourthTestKit), 4);

        // testing for adding a kit already in the map with an invalid quantity
        testOrder.addKits(secondTestKit, -1);
        // if addKits was implemented improperly this will be 4 instead of 5 
        assertEquals(testOrder.getKitQuantity(secondTestKit), 5); 

        // testing for adding a kit not in the map with an invalid quantity
        Kit fifthTestKit = new Kit(5, "5", 5.0f, 5, new ArrayList<Integer>());
        testOrder.addKits(fifthTestKit, 0);
        assertEquals(testOrder.getKitQuantity(fifthTestKit), 0);
        // verifying that the kit wasn't added to the order map
        Map<Kit, Integer> resultingMap = testOrder.getAllKits();
        assertEquals(resultingMap.get(fifthTestKit), null); 
    }

    /**
     * This method tests the getId method in the Order class
     */
    @Test
    public void testGetId() {
        assertEquals(testOrder.getId(), testId);
    }

}

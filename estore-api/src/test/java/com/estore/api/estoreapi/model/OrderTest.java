package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

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
    //private Map<Kit, Integer> testMap;
    private ArrayList<Kit> testList;
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
        //testMap = new HashMap<>();
        testList = new ArrayList<>();
        firstTestKit = new Kit(1, "abc", 5.0f, 10, new ArrayList<Integer>());
        secondTestKit = new Kit(2, "def", 5.0f, 10, new ArrayList<Integer>());
        thirdTestKit = new Kit(3, "ghi", 5.0f, 10, new ArrayList<Integer>());
        /*testMap.put(firstTestKit, 3);
        testMap.put(secondTestKit, 5);
        testMap.put(thirdTestKit, 7);*/
        testList.add(firstTestKit);
        testList.add(secondTestKit);
        testList.add(thirdTestKit);
        testId = 1;
        testOrder = new Order(testId, testUser, testList);
    }

    /**
     * This method tests the getKits method in the Order class
     */
    @Test
    public void testGetKits() {
        // these should be equivalent since the order class doesn't create a new list, it just uses the one provided
        assertEquals(testOrder.getKits(), testList); 
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
    
    @Test
    public void testAddKit() {
        // adding a kit that doesn't exist
        Kit testKit = new Kit(100, "test", 50.0f, 10, new ArrayList<>());
        testOrder.addKit(testKit);
        ArrayList<Kit> resultingList = testOrder.getKits();
        assertTrue(resultingList.contains(testKit));
        // adding a kit that already exists
        int initialLength = testOrder.getKits().size();
        testOrder.addKit(firstTestKit); // this kit already exists in the order
        assertEquals(initialLength, testOrder.getKits().size());
    }

    /**
     * This method tests the clearKitFromOrder method in the Order class
     */
    @Test
    public void testClearKitFromOrder() {
        Kit clearedKit = firstTestKit;
        testOrder.clearKitFromOrder(clearedKit); 
        // once we've cleared the kit from the order, it should return a quantity of 0
        ArrayList<Kit> resultingList = testOrder.getKits();
        for (Kit k : resultingList) {
            assertNotEquals(clearedKit.getName(), k.getName());
        }
    }

    /**
     * This method tests the getId method in the Order class
     */
    @Test
    public void testGetId() {
        assertEquals(testOrder.getId(), testId);
    }

    /**
     * This method tests the containsMatchingKit method in the Order class
     */
    @Test
    public void testContainsMatchingKit() {
        // verifying no matches are found if the string doesn't match the names of any of the kits
        String noMatchesString = "zzzzz"; 
        assertFalse(testOrder.containsMatchingKit(noMatchesString));
        // verifying a match is found if the string matches the name of 1 of the kits
        String matchString = "a"; 
        assertTrue(testOrder.containsMatchingKit(matchString));
        // verifying a match is found if the string is empty
        assertTrue(testOrder.containsMatchingKit(""));
    }

    /**
     * This method tests the getUser method in the Order class
     */
    @Test
    public void testGetUser() {
        assertEquals(testOrder.getUser(), testUser);
    }

}

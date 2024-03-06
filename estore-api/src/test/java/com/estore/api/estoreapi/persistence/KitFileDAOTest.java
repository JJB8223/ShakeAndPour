package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.Kit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;

/**
 * Test the Inventory File DAO class
 *
 * @author Duncan French dlf3550
 */
@Tag("Persistence-tier")
public class KitFileDAOTest {
    KitFileDAO kitFileDAO;
    Kit[] testKits;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setupTests()  throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testKits = new Kit[3];

        ArrayList<Integer> firstKitList = new ArrayList<>();
        firstKitList.add(1);
        firstKitList.add(2);
        testKits[0] = new Kit(1, "Test Kit Alpha", 3.14f, 5, firstKitList);

        ArrayList<Integer> secondKitList = new ArrayList<>();
        secondKitList.add(2);
        secondKitList.add(3);
        testKits[1] = new Kit(2, "Test Kit Bravo", 2.71f, 3, secondKitList);

        ArrayList<Integer> thirdKitList = new ArrayList<>();
        thirdKitList.add(3);
        thirdKitList.add(1);
        testKits[2] = new Kit(3, "Test Kit Charlie", 7f, 0, secondKitList);

        when(mockObjectMapper.readValue(new File("this_doesn't_matter.txt"),Kit[].class)).thenReturn(testKits);
        kitFileDAO = new KitFileDAO("this_doesn't_matter.txt", mockObjectMapper);
    }

    @Test
    public void testGetKitExists() {
        // Setup
        Kit expectedKit = testKits[1];
        int testId = 2;
        // invoke
        Kit actualKit = kitFileDAO.getKit(testId);
        // analyze
        assertEquals(expectedKit, actualKit);
    }

    @Test
    public void testGetKitNotExists() {
        // Setup
        int testId = 4; 
        // invoke
        Kit actualKit = kitFileDAO.getKit(testId);
        // analyze
        assertEquals(actualKit, null);
    }

    @Test
    public void testGetKits() {
        // Invoke
        Kit[] resultingKits = kitFileDAO.getKits();
        // Analyze
        for (int i = 0; i < resultingKits.length; i++) {
            assertEquals(resultingKits[i], testKits[i]);
        }
    }

    @Test
    public void testFindKitsEmpty() {
        // Setup
        Kit[] expectedKits = testKits; // we expect to get all the kits upon entering an empty string
        // Invoke
        Kit[] resultingKits = kitFileDAO.findKits("");
        // Analyze
        for (int i = 0; i < resultingKits.length; i++) {
            assertEquals(expectedKits[i], resultingKits[i]);
        }
    }

    @Test
    public void testFindKitsNoMatches() {
        // invoke
        Kit[] resultingKits = kitFileDAO.findKits("Zebra"); 
        // analyze
        assertEquals(resultingKits.length, 0); // none of the kits contain this string
    }

    @Test
    public void testFindKitsMatches() {
        // Setup
        Kit expectedKit = testKits[2];
        int expectedMatches = 1; // only 1 kit has Charlie in the name
        // invoke
        Kit[] resultingKits = kitFileDAO.findKits("Charlie");
        // analyze
        assertEquals(resultingKits.length, expectedMatches);
        assertEquals(resultingKits[0], expectedKit);
    }

    @Test
    public void testDeleteKitExists() throws IOException {
        // Setup 
        int deletedId = 1;
        int expectedResultingLength = 2;
        // invoke
        boolean result = kitFileDAO.deleteKit(deletedId);
        Kit[] resultingKits = kitFileDAO.getKits();
        // analyze
        assertTrue(result);
        assertEquals(resultingKits.length, expectedResultingLength);
        for (int i = 0; i < resultingKits.length; i++) {
            assertEquals(resultingKits[i], testKits[i + 1]); // we deleted the first kit so we increase the checked index by 1
        }
    }

    @Test
    public void testDeleteKitNotExists() throws IOException {
        // Setup
        int deletedId = 4;
        int expectedResultingLength = 3;
        // invoke
        boolean result = kitFileDAO.deleteKit(deletedId);
        // analyze
        assertFalse(result);
        assertEquals(kitFileDAO.getKits().length, expectedResultingLength);
    }

    @Test
    public void testCreateKit() throws IOException {
        // Setup
        int createdId = 4;
        String createdName = "Test Kit Delta"; 
        float createdPrice = 9.5f;
        int createdQuantity = 2; 
        ArrayList<Integer> idList = new ArrayList<>();
        Kit createdKit = new Kit(createdId, createdName, createdPrice, createdQuantity, idList);
        int expectedResultingLength = 4;
        // invoke
        kitFileDAO.createKit(createdKit);
        Kit[] resultingKits = kitFileDAO.getKits();
        // analyze
        assertEquals(resultingKits.length, expectedResultingLength);
        // checking shallow equality doesn't work because the KitFileDAO class serializes and deserializes its data
        assertEquals(createdKit.getId(), resultingKits[3].getId());
        assertEquals(createdKit.getName(), resultingKits[3].getName());
        assertEquals(createdKit.getPrice(), resultingKits[3].getPrice());
        assertEquals(createdKit.getQuantity(), resultingKits[3].getQuantity());
        assertEquals(createdKit.getProductsInKit(), resultingKits[3].getProductsInKit());
    }

    @Test
    public void testUpdateKitExists() throws IOException {
        // Setup
        int updatedId = 3;
        String updatedName = "Test Kit Three";
        float updatedPrice = 1.1f;
        int updatedQuantity = 100;
        ArrayList<Integer> idList = new ArrayList<>();
        Kit updatedKit = new Kit(updatedId, updatedName, updatedPrice, updatedQuantity, idList);
        // invoke
        Kit response = kitFileDAO.updateKit(updatedKit);
        Kit[] resultingKits = kitFileDAO.getKits();
        // analyze
        assertEquals(updatedKit, response);
        // checking shallow equality doesn't work because the KitFileDAO class serializes and deserializes its data
        assertEquals(updatedKit.getId(), resultingKits[2].getId());
        assertEquals(updatedKit.getName(), resultingKits[2].getName());
        assertEquals(updatedKit.getPrice(), resultingKits[2].getPrice());
        assertEquals(updatedKit.getQuantity(), resultingKits[2].getQuantity());
        assertEquals(updatedKit.getProductsInKit(), resultingKits[2].getProductsInKit());
    }

    @Test
    public void testUpdateKitNotExists() throws IOException {
        // Setup
        int updatedId = 5;
        String updatedName = "Test Kit Echo";
        float updatedPrice = 1.1f;
        int updatedQuantity = 100;
        ArrayList<Integer> idList = new ArrayList<>();
        Kit updatedKit = new Kit(updatedId, updatedName, updatedPrice, updatedQuantity, idList);
        // invoke
        Kit response = kitFileDAO.updateKit(updatedKit);
        // analyze
        assertEquals(response, null);
    }

}

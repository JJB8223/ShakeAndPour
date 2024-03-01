package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.Kit;
import com.estore.api.estoreapi.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        // setup
        Kit expectedKit = testKits[1];
        int testId = 2;
        // run
        Kit actualKit = kitFileDAO.getKit(testId);
        // check
        assertEquals(expectedKit, actualKit);
    }

    @Test
    public void testGetKitNotExists() {
        // setup
        int testId = 4; 
        // run
        Kit actualKit = kitFileDAO.getKit(testId);
        assertEquals(actualKit, null);
    }

    @Test
    public void getKits() {

    }

    @Test
    public void testFindKitsEmpty() {
        
    }

    @Test
    public void testFindKitsNoMatches() {
        
    }

    @Test
    public void testFindKitsMatches() {
        
    }

    @Test
    public void testDeleteKitExists() {

    }

    @Test
    public void testDeleteKitNotExists() {

    }

    @Test
    public void testDeleteKitIOException() {

    }

}

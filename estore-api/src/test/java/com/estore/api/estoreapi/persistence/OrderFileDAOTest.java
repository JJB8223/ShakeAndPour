package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.Kit;
import com.estore.api.estoreapi.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests the OrderFileDAO class
 * 
 * @author Duncan French
 */
public class OrderFileDAOTest {

    private OrderFileDAO orderDAO;
    private Order[] testOrders;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setupTests() throws IOException {
        objectMapper = mock(ObjectMapper.class);
        testOrders = new Order[3];

        ArrayList<Kit> firstTestList = new ArrayList<>();
        firstTestList.add(new Kit(1, "yummy", 9.0f, 5, new ArrayList<Integer>()));
        testOrders[0] = new Order(1, "test", firstTestList);

        ArrayList<Kit> secondTestList = new ArrayList<>();
        secondTestList.add(new Kit(2, "slimy", 11.0f, 10, new ArrayList<Integer>()));
        testOrders[1] = new Order(2, "test", secondTestList);

        ArrayList<Kit> thirdTestList = new ArrayList<>();
        thirdTestList.add(new Kit(3, "gummy", 7.0f, 10, new ArrayList<Integer>()));
        thirdTestList.add(new Kit(4, "runny", 5.0f, 7, new ArrayList<Integer>()));
        testOrders[2] = new Order(3, "zorg", thirdTestList);

        when(objectMapper
                .readValue(new File("doesnt_matter.txt"),Order[].class))
                .thenReturn(testOrders);
        orderDAO = new OrderFileDAO("doesnt_matter.txt", objectMapper);
    }

    @Test
    public void testGetOrder() {
        // testing with an id associated with an existing order
        assertEquals(orderDAO.getOrder(3), testOrders[2]);

        // testing with an id not associated with any order
        assertEquals(orderDAO.getOrder(999), null);
    }

    @Test
    public void testGetOrders() throws IOException {
        // testing with a username that doesn't have any orders associated with it
        assertEquals(orderDAO.getOrders("no orders?").length, 0); 

        // testing with a username with 2 orders associated with it
        Order[] twoOrderResponse = orderDAO.getOrders("test");
        assertEquals(twoOrderResponse.length, 2);
        assertEquals(twoOrderResponse[0].getUser(), twoOrderResponse[1].getUser(), "test");
        assertEquals(twoOrderResponse[0].getId(), 1);
        assertEquals(twoOrderResponse[1].getId(), 2);
        assertEquals(twoOrderResponse[0].getKits(), testOrders[0].getKits());
        assertEquals(twoOrderResponse[1].getKits(), testOrders[1].getKits());

        // testing with a username with 1 order associated with it
        Order[] oneOrderResponse = orderDAO.getOrders("zorg");
        assertEquals(oneOrderResponse.length, 1);
        assertEquals(oneOrderResponse[0].getUser(), "zorg");
        assertEquals(oneOrderResponse[0].getId(), 3);
        assertEquals(oneOrderResponse[0].getKits(), testOrders[2].getKits());
    }

    @Test
    public void testCreateOrder() throws IOException {
        // Given
        String username = "testUser";
        ArrayList<Kit> kits = new ArrayList<>();

        // When
        Order createdOrder = orderDAO.createOrder(username, kits);

        // Then
        assertNotNull(createdOrder, "Order should not be null");
        assertEquals(username, createdOrder.getUser(), "Username should match");
        assertEquals(kits, createdOrder.getKits(), "Kits list should be equal");

        Order retrievedOrder = orderDAO.getOrder(createdOrder.getId());
        assertNotNull(retrievedOrder, "Retrieved order should not be null");
        assertEquals(createdOrder.getId(), retrievedOrder.getId(), "Retrieved order ID should match");
        assertEquals(createdOrder.getUser(), retrievedOrder.getUser(), "Retrieved username should match");
        assertEquals(createdOrder.getKits(), retrievedOrder.getKits(), "Retrieved kits list should be equal");
    }

    @Test
    public void testFindOrders() throws IOException {
        // testing with a string that should have no matches
        Order[] noMatchingOrders = orderDAO.findOrders("n", "test"); 
        assertEquals(noMatchingOrders.length, 0);

        // testing with a string that should have 1 match
        Order[] oneMatchingOrder = orderDAO.findOrders("yummy", "test");
        assertEquals(oneMatchingOrder.length, 1);
        assertEquals(oneMatchingOrder[0], testOrders[0]);

        // testing with a string that should have 2 matches
        Order[] twoMatchingOrders = orderDAO.findOrders("y", "test");
        assertEquals(twoMatchingOrders.length, 2);
        assertEquals(twoMatchingOrders[0], testOrders[0]);
        assertEquals(twoMatchingOrders[1], testOrders[1]);
    }
}

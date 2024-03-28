package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.model.Kit;
import com.estore.api.estoreapi.model.Order;
import com.estore.api.estoreapi.persistence.OrderDAO;

/**
 * Test the OrderController class
 * 
 * @author Duncan French
 */
@Tag("Controller-tier")
public class OrderControllerTest {

    private OrderDAO orderDao;
    private OrderController orderController;

    @BeforeEach
    public void setupOrderController() {
        orderDao = mock(OrderDAO.class);
        orderController = new OrderController(orderDao);
    }

    @Test
    public void testCreateOrder() throws IOException {
        // order created successfully
        // setup
        Order createdOrderSuccess = new Order(10, "testUser", new HashMap<Kit, Integer>());
        when(orderDao.createOrder(createdOrderSuccess)).thenReturn(createdOrderSuccess);
        // invoke
        ResponseEntity<Order> successResponse = orderController.createOrder(createdOrderSuccess);
        // analyze
        assertEquals(HttpStatus.CREATED, successResponse.getStatusCode());
        assertEquals(createdOrderSuccess, successResponse.getBody());

        // conflict on creating order
        // setup 
        Order createdOrderConflict = new Order(11, "testUser", new HashMap<Kit, Integer>());
        when(orderDao.createOrder(createdOrderConflict)).thenReturn(null);
        // invoke
        ResponseEntity<Order> conflictResponse = orderController.createOrder(createdOrderConflict);
        // analyze
        assertEquals(HttpStatus.CONFLICT, conflictResponse.getStatusCode());

        // IOException on creating order
        // setup
        Order createdOrderException = new Order(12, "testUser", new HashMap<Kit, Integer>());
        when(orderDao.createOrder(createdOrderException)).thenThrow(new IOException());
        // invoke
        ResponseEntity<Order> exceptionResponse = orderController.createOrder(createdOrderException);
        // analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exceptionResponse.getStatusCode());
    }
    
    @SuppressWarnings("null") // emptyResponse.getBody().length has a null warning but can't actually be null
    @Test
    public void testSearchOrders() throws IOException {
        String searchUser = "testUser";
        // some orders returned
        // setup
        String responseName = "yes";
        Order[] orderResponse = new Order[]{new Order(100, "testUser", new HashMap<Kit, Integer>())};
        when(orderDao.findOrders(responseName, searchUser)).thenReturn(orderResponse);
        // invoke
        ResponseEntity<Order[]> normalResponse = orderController.searchOrders(responseName, searchUser);
        // analyze
        assertEquals(HttpStatus.OK, normalResponse.getStatusCode());
        assertEquals(orderResponse[0], normalResponse.getBody()[0]);

        // no orders returned
        // setup
        String emptyName = "no";
        when(orderDao.findOrders(emptyName, searchUser)).thenReturn(new Order[0]);
        // invoke
        ResponseEntity<Order[]> emptyResponse = orderController.searchOrders(emptyName, searchUser);
        // analyze
        assertEquals(HttpStatus.OK, emptyResponse.getStatusCode());
        assertEquals(0, emptyResponse.getBody().length); 

        // IOException while searching
        // setup
        String exceptionName = "error";
        when(orderDao.findOrders(exceptionName, searchUser)).thenThrow(new IOException());
        // invoke
        ResponseEntity<Order[]> exceptionResponse = orderController.searchOrders(exceptionName, searchUser);
        // analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exceptionResponse.getStatusCode());
    }

    @Test
    public void testGetOrder() {
        // searched order id exists
        // setup
        int foundId = 10;
        Order foundOrder = new Order(10, "testUser", new HashMap<Kit, Integer>());
        when(orderDao.getOrder(foundId)).thenReturn(foundOrder);
        // invoke
        ResponseEntity<Order> foundResponse = orderController.getOrder(foundId);
        // analyze
        assertEquals(HttpStatus.OK, foundResponse.getStatusCode());
        assertEquals(foundOrder, foundResponse.getBody());

        // searched order id doesn't exist
        int notFoundId = 4004;
        when(orderDao.getOrder(notFoundId)).thenReturn(null);
        // invoke
        ResponseEntity<Order> notFoundResponse = orderController.getOrder(notFoundId);
        // anaylze
        assertEquals(HttpStatus.NOT_FOUND, notFoundResponse.getStatusCode());
    }

    @Test
    public void testGetOrders() throws IOException {
        // no error occurs
        // setup
        String validUser = "valid";
        when(orderDao.getOrders(validUser)).thenReturn(new Order[0]);
        // invoke
        ResponseEntity<Order[]> validResponse = orderController.getOrders(validUser);
        // analyze
        assertEquals(HttpStatus.OK, validResponse.getStatusCode());
        // IOException occurs
        // setup
        String exceptionUser = "error";
        when(orderDao.getOrders(exceptionUser)).thenThrow(new IOException());
        // invoke
        ResponseEntity<Order[]> exceptionResponse = orderController.getOrders(exceptionUser);
        // analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exceptionResponse.getStatusCode());
    }
}

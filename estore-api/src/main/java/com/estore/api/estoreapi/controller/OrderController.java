package com.estore.api.estoreapi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.Kit;
import com.estore.api.estoreapi.model.Order;
import com.estore.api.estoreapi.controller.UserController;
import com.estore.api.estoreapi.persistence.OrderDAO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controller for managing updating and getting a customer's order history.
 * This class provides endpoints for getting the order history of a specified user, 
 * searching that user's order history, adding to a user's order history, and getting
 * a specific order using a unique id
 * 
 * @author Duncan French
 */
@RestController
@RequestMapping("/orders") 
public class OrderController {
    
    private static final Logger LOG = Logger.getLogger(OrderController.class.getName());

    private final OrderDAO orderDao;


    /**
     * Constructs an OrderController with the specified order DAO
     * 
     * @param orderDao the DAO responsible for order operations. This is injected by the spring framework
     */
    public OrderController(OrderDAO orderDao) {
        this.orderDao = orderDao;
    }

    /**
     * Creates a {@linkplain Order Order} with the provided Order object
     *
     * @param order - The {@link Order Order} to create
     *
     * @return ResponseEntity with created {@link Order Order} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Order Order} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * 
     * @throws IOException if error occurs with the server
     */
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestParam("username") String username, @RequestParam("kits") String kitsJson) {
        LOG.info("POST /orders/create/" + username);

        try {
            // Deserialize kitsJson into an ArrayList<Kit>
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<Kit> kits = (ArrayList<Kit>) mapper.readValue(kitsJson, new TypeReference<List<Kit>>(){});

            Order createdOrder = orderDao.createOrder(username, kits);
            if (createdOrder == null) { // Attempting to create an order that conflicts with an existing order
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (IOException e) { // Error in deserialization or server storage
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Order Orders} that contain Kits with the
     * provided text in name and were bought by the provided user
     *
     * @param name Contains the text used to find the {@linkplain Order Orders}
     * @param user Contains the user who needs to have bought any returned {@linkplain Order Orders}
     *
     * @return ResponseEntity with array of {@linkplain Order Order} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * 
     * @throws IOException if error occurs trying to get any Orders
     */
    @GetMapping("/{name}/")
    public ResponseEntity<Order[]> searchOrders(@PathVariable String name, @RequestParam String user) {
        LOG.info("GET /orders/" + name + "?user=" + user);
        try {
            Order[] orders = orderDao.findOrders(name, user);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Order Orders} purchased by a specific user
     *
     * @param user Contains the user who bought the returned {@linkplain Order Orders}
     *
     * @return ResponseEntity with array of {@linkplain Order Order} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * 
     * @throws IOException if error occurs trying to get any Orders
     */
    @GetMapping("/{user}")
    public ResponseEntity<Order[]> getOrders(@PathVariable String user) {
        LOG.info("GET /orders/" + user);
        try {
            Order[] orders = orderDao.getOrders(user);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for a {@linkplain Order Order} for the given id
     *
     * @param id The id used to locate the {@linkplain Order Order}
     *
     * @return ResponseEntity with located {@linkplain Order Order} object and HTTP status of OK<
     * ResponseEntity with HTTP status of NOT_FOUND if not found
     */
    @GetMapping("getSpecific/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable int id) {
        LOG.info("GET /orders/" + id);
        
        Order order = orderDao.getOrder(id);
        if (order != null){
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } 
    }

}

package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Order;

/**
 * Defines the interface for Order object persistence
 * 
 * @author Duncan French
 */
public interface OrderDAO {

    /**
     * Creates and saves a {@linkplain Order Order}
     *
     * @param Order {@linkplain Order Product} object to be created and saved
     * <br>
     * The id of the Order object is ignored and a new unique id is assigned
     *
     * @return new {@link Order Product} if successful, null otherwise
     *
     * @throws IOException if an issue with underlying storage
     */
    Order createOrder(Order order) throws IOException;

    /**
     * Finds all {@linkplain Order Orders} that contain kits with names that contain the given text that were
     * purchased by the specified user
     * 
     * @param containsText The text to match against
     * @param username The username orders must be associated with 
     * 
     * @return An array of {@link Order Orders} who contain kits with names that match the given 
     * text and were purchased by the specified user, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Order[] findOrders(String containsText, String username) throws IOException;

    /**
     * Finds all {@linkplain Order Orders} that were
     * purchased by the specified user
     * 
     * @param username The username orders must be associated with 
     * 
     * @return An array of {@link Order Orders} who were purchased by the specified user, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Order[] getOrders(String username) throws IOException;

    /**
     * Retrieves a {@linkplain Order Order} matching the specified id
     * 
     * @param id the id that orders are checked to match
     * 
     * @return The {@linkplain Order Order} matching the id parameter
     */
    Order getOrder(int id);

}

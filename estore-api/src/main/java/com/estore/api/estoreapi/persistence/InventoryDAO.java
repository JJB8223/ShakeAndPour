package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.Product;

import java.io.IOException;

/**
 * Defines the interface for Inventory object persistence
 *
 * @author Matthew Morrison msm8275
 * @author David Dobbins dpd8504
 */
public interface InventoryDAO {
    /**
     * Creates and saves a {@linkplain Product Product}
     *
     * @param Product {@linkplain Product Product} object to be created and saved
     * <br>
     * The id of the Product object is ignored and a new unique id is assigned
     *
     * @return new {@link Product Product} if successful, false otherwise
     *
     * @throws IOException if an issue with underlying storage
     */
    Product createProduct(Product Product) throws IOException;

    /**
     * Retrieves all {@linkplain Product Products}
     * 
     * @return An array of {@link Product Product} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product[] getProducts() throws IOException;


    /**
     * Finds all {@linkplain Product Products} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Product Products} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product[] findProducts(String containsText) throws IOException;

    /**
     * Deletes a {@linkplain Product Product} with the given id
     * 
     * @param id The id of the {@link Product Product}
     * 
     * @return true if the {@link Product Product} was deleted
     * <br>
     * false if Product with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteProduct(int id) throws IOException;
}

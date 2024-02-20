package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.Product;

import java.io.IOException;

/**
 * Defines the interface for Inventory object persistence
 *
 * @author Matthew Morrison msm8275
 * @author David Dobbins dpd8504
 * @author Akhil Devarapalli ad7171
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
     * Updates and saves a {@linkplain Product Product}
     * 
     * @param {@link Product Product} object to be updated and saved
     * 
     * @return updated {@link Product Product} if successful, null if
     * {@link Product Product} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Product updateProduct(Product product) throws IOException;
    
}

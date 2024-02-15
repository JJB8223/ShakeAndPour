package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.Product;

import java.io.IOException;

/**
 * Defines the interface for Inventory object persistance
 *
 * @author Matthew Morrison msm8275
 */
public interface InventoryDAO {
    /**
     * Creates and saves a {@linkplain Product Product}
     *
     * @param Product {@linkplain Product Product} object to be created and saved
     * <br>
     * The id of the Product object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Product Product} if successful, false otherwise
     *
     * @throws IOException if an issue with underlying storage
     */
    Product createProduct(Product Product) throws IOException;
}

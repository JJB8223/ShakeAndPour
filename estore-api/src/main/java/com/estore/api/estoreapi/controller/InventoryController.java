package com.estore.api.estoreapi.controller;


import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.persistence.InventoryDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("inventory")
public class InventoryController{
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());

    private InventoryDAO InventoryDao;

    /**
     * Creates a REST API controller to reponds to requests
     * @param InventoryDao The {@link InventoryDAO Product Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public InventoryController(InventoryDAO InventoryDao){this.InventoryDao = InventoryDao;}


    /**
     * Creates a {@linkplain Product Product} with the provided Product object
     *
     * @param Product - The {@link Product Product} to create
     *
     * @return ResponseEntity with created {@link Product Product} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Product Product} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * @throws IOException if error occurs with the server
     */
    @PostMapping("")
    public ResponseEntity<Product> createProduct(@RequestBody Product Product) throws IOException {
        LOG.info("POST /inventory/product " + Product);
        try {
            Product createdProduct = InventoryDao.createProduct(Product);
            if (createdProduct == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

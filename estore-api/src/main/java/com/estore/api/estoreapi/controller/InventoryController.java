package com.estore.api.estoreapi.controller;


import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.estore.api.estoreapi.persistence.InventoryDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("inventory")
public class InventoryController{
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());

    private InventoryDAO inventoryDao;

    /**
     * Creates a REST API controller to reponds to requests
     * @param inventoryDao The {@link InventoryDAO Product Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public InventoryController(InventoryDAO inventoryDao){
        this.inventoryDao = inventoryDao;
    }


    /**
     * Responds to the GET request for all {@linkplain Product products} whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link Product products}
     * 
     * @return ResponseEntity with array of {@link Product Product} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all products that contain the text "ma"
     * GET http://localhost:8080/products/?name=ma
     * @throws IOException 
     */
    @GetMapping("/")
    public ResponseEntity<Product[]> searchProducts(@RequestParam String name) throws IOException {
        LOG.info("GET /inventory/?name="+name);
        try {
            Product[] products = inventoryDao.findProducts(name);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
    @PostMapping("/product")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) throws IOException {
        LOG.info("POST /inventory/product " + product);
        try {
            Product createdProduct = inventoryDao.createProduct(product);
            if (createdProduct == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Product Product} with the provided {@linkplain Product Product} object, if it exists
     * 
     * @param Product The {@link Product Product} to update
     * 
     * @return ResponseEntity with updated {@link Product Product} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Product> updateProduct(@RequestBody Product Product) {
        LOG.info("PUT /products " + Product);
        try {
            Product status = inventoryDao.updateProduct(Product);
            if (status == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(status, HttpStatus.OK);
            }
        } catch (IOException e) { 
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Responds to the GET request for all {@linkplain Product products}
     * 
     * @return ResponseEntity with array of {@link Product Product} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * @throws IOException 
     */
    @GetMapping("")
    public ResponseEntity<Product[]> getProducts() throws IOException {
        LOG.info("GET /inventory");
        try {
            Product[] products = inventoryDao.getProducts();
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for a {@linkplain Product product} for the given id
     *
     * @param id The id used to locate the {@link Product product}
     *
     * @return ResponseEntity with created {@link Product Product} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        LOG.info("GET /inventory/product/" + id);
        try {
            Product product = inventoryDao.getProduct(id);
            if (product != null)
                return new ResponseEntity<Product>(product,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Product Product} with the given id
     * 
     * @param id The id of the {@link Product Product} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable int id) {
        LOG.info("DELETE /inventory/product/" + id);
        try {
            boolean status = inventoryDao.deleteProduct(id);
            if (!status) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

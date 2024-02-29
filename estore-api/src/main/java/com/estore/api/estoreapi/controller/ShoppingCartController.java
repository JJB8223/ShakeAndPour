package com.estore.api.estoreapi.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.estore.api.estoreapi.persistence.InventoryDAO;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for handling shopping cart operations within the e-store API.
 * This class provides endpoints for adding products to and removing products from a shopping cart.
 * It interacts with the InventoryDAO to check product availability and update stock levels.
 * 
 * @author David Dobbins dpd8504
 */

@RestController
@RequestMapping("/cart") // Base path for all methods in this controller
public class ShoppingCartController {

    private static final Logger LOG = Logger.getLogger(ShoppingCartController.class.getName());

    private final InventoryDAO inventoryDao;
    private final ShoppingCart shoppingCart;
    
    /**
     * Constructs a ShoppingCartController with the specified inventory DAO and shopping cart.
     * 
     * @param inventoryDao the DAO responsible for inventory operations
     * @param shoppingCart the shopping cart for the current user session
     */
    public ShoppingCartController(InventoryDAO inventoryDao, ShoppingCart shoppingCart) {
        this.inventoryDao = inventoryDao;
        this.shoppingCart = shoppingCart;
    }

    /**
     * Adds a product to the shopping cart. Validates that the product exists and that there is
     * sufficient stock before adding it to the cart. Updates the inventory to reflect the new stock level.
     * 
     * @param id the ID of the product to add
     * @param quantity the quantity of the product to add
     * @return ResponseEntity representing the result of the operation (OK, BAD_REQUEST, NOT_FOUND, INTERNAL_SERVER_ERROR)
     */
    @PostMapping("/add")
    public ResponseEntity<Void> addtoCart(@PathVariable int id, @RequestParam int quantity) {
        LOG.info("POST /cart/add/id/?quantity=");
        try {
            Product product = inventoryDao.getProduct(id);
            if (product != null) {
                if (quantity <= product.getQuantity() && quantity > 0) {
                    shoppingCart.addProduct(product, quantity);
                    int prevQuantity = product.getQuantity();
                    product.setQuantity(prevQuantity - quantity);
                    inventoryDao.updateProduct(product);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Removes a product from the shopping cart. Validates that the product exists in the cart before
     * removing the specified quantity. Updates the inventory to reflect the returned stock level.
     * 
     * @param id the ID of the product to remove
     * @param quantity the quantity of the product to remove
     * @return ResponseEntity representing the result of the operation (OK, NOT_FOUND, INTERNAL_SERVER_ERROR)
     */
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromCart(@PathVariable int id, @RequestParam int quantity) {
        LOG.info("DELETE /cart/remove/id/?quantity=");
        try {
            Product product = inventoryDao.getProduct(id);
            if (shoppingCart.containsProduct(product)) {
                int newQuantity = product.getQuantity() + quantity;
                if (shoppingCart.getProductQuantity(product) < quantity) {
                    newQuantity = product.getQuantity() + shoppingCart.getProductQuantity(product);
                }
                product.setQuantity(newQuantity);
                inventoryDao.updateProduct(product);
                shoppingCart.removeProduct(product, quantity);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
    * Retrieves and returns all the products currently in the shopping cart.
    * This endpoint allows the client to view all items in the cart, along with their quantities.
    * 
    * @return A ResponseEntity containing a map of products to their quantities in the cart and the HTTP status code.
    * @throws IOException if an I/O error occurs during product retrieval.
    */
    @GetMapping("")
    public ResponseEntity<Map<Product, Integer>> getCartProducts() throws IOException {
        LOG.info("GET /cart");
        Map<Product, Integer> cartItems = shoppingCart.getProducts();
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }
}

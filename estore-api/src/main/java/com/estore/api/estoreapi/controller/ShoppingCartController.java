package com.estore.api.estoreapi.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.estore.api.estoreapi.persistence.InventoryDAO;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/cart") // Base path for all methods in this controller
public class ShoppingCartController {

    private static final Logger LOG = Logger.getLogger(ShoppingCartController.class.getName());

    private final InventoryDAO inventoryDao;
    private final ShoppingCart shoppingCart;
    
    public ShoppingCartController(InventoryDAO inventoryDao, ShoppingCart shoppingCart) {
        this.inventoryDao = inventoryDao;
        this.shoppingCart = shoppingCart;
    }

    @PostMapping("/cart/add")
    public ResponseEntity<Void> addtoCart(@PathVariable int id, @RequestParam int quantity) {
        LOG.info("POST /cart/add/" + id + " qty: " + quantity);
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

    @DeleteMapping("/cart/remove")
    public ResponseEntity<Void> removeFromCart(@PathVariable int id, @RequestParam int quantity) {
        LOG.info("DELETE /cart/remove/" + id + " qty: " + quantity);
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
}

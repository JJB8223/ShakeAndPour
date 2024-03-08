package com.estore.api.estoreapi.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.estore.api.estoreapi.model.Kit;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.estore.api.estoreapi.persistence.KitDAO;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for handling shopping cart operations within the e-store API.
 * This class provides endpoints for adding kits to and removing kits from a shopping cart.
 * It interacts with the InventoryDAO to check kit availability and update stock levels.
 * 
 * @author David Dobbins dpd8504
 */

@RestController
@RequestMapping("/cart") // Base path for all methods in this controller
public class ShoppingCartController {

    private static final Logger LOG = Logger.getLogger(ShoppingCartController.class.getName());

    private final KitDAO kitDao;
    private ShoppingCart shoppingCart;
    
    /**
     * Constructs a ShoppingCartController with the specified kit DAO and shopping cart.
     * 
     * @param kitDao the DAO responsible for kit operations
     * @param shoppingCart the shopping cart for the current user session
     */
    public ShoppingCartController(KitDAO kitDao) {
        this.kitDao = kitDao;
        this.shoppingCart = new ShoppingCart();
    }

    /**
     * Adds a kit to the shopping cart. Validates that the kit exists and that there is
     * sufficient stock before adding it to the cart. Updates the kit to reflect the new stock level.
     * 
     * @param id the ID of the kit to add
     * @param quantity the quantity of the kit to add
     * @return ResponseEntity representing the result of the operation (OK, BAD_REQUEST, NOT_FOUND, INTERNAL_SERVER_ERROR)
     */
    @PostMapping("/add")
    public ResponseEntity<Void> addtoCart(@PathVariable int id, @RequestParam int quantity) {
        LOG.info("POST /cart/add/id/?quantity=");
        try {
            Kit kit = kitDao.getKit(id);
            if (kit != null) {
                if (quantity <= kit.getQuantity() && quantity > 0) {
                    shoppingCart.addKit(kit, quantity);
                    int prevQuantity = kit.getQuantity();
                    kit.setQuantity(prevQuantity - quantity);
                    kitDao.updateKit(kit);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    shoppingCart.addKit(kit, kit.getQuantity());
                    kit.setQuantity(0);
                    return new ResponseEntity<>(HttpStatus.OK);
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
     * Removes a kit from the shopping cart. Validates that the kit exists in the cart before
     * removing the specified quantity. Updates the kit to reflect the returned stock level.
     * 
     * @param id the ID of the kit to remove
     * @param quantity the quantity of the kit to remove
     * @return ResponseEntity representing the result of the operation (OK, NOT_FOUND, INTERNAL_SERVER_ERROR)
     */
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromCart(@PathVariable int id, @RequestParam int quantity) {
        LOG.info("DELETE /cart/remove/id/?quantity=");
        try {
            Kit kit = kitDao.getKit(id);
            if (kit == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (shoppingCart.containsKit(kit)) {
                int newQuantity = kit.getQuantity() + quantity;
                if (shoppingCart.getKitQuantity(kit) < quantity) {
                    newQuantity = kit.getQuantity() + shoppingCart.getKitQuantity(kit);
                }
                kit.setQuantity(newQuantity);
                kitDao.updateKit(kit);
                shoppingCart.removeKit(kit, quantity);
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
    * Retrieves and returns all the kits currently in the shopping cart.
    * This endpoint allows the client to view all items in the cart, along with their quantities.
    * 
    * @return A ResponseEntity containing a map of kits to their quantities in the cart and the HTTP status code.
    * @throws IOException if an I/O error occurs during kit retrieval.
    */
    @GetMapping("")
    public ResponseEntity<Map<Kit, Integer>> getCartKits() throws IOException {
        LOG.info("GET /cart");
        Map<Kit, Integer> cartItems = shoppingCart.getKits();
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }
}

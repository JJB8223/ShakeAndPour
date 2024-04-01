package com.estore.api.estoreapi.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.estore.api.estoreapi.model.Kit;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.estore.api.estoreapi.persistence.KitDAO;
import com.estore.api.estoreapi.model.ShoppingCartKit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    private Map<Integer, ShoppingCart> userCarts = new HashMap<>();

    
    /**
     * Constructs a ShoppingCartController with the specified kit DAO and shopping cart.
     * 
     * @param kitDao the DAO responsible for kit operations
     * @param shoppingCart the shopping cart for the current user session
     */
    public ShoppingCartController(KitDAO kitDao) {
        this.kitDao = kitDao;
    }

    private ShoppingCart getShoppingCartForUser(Integer userId) {
        return userCarts.computeIfAbsent(userId, k -> new ShoppingCart());
    }

    /**
     * Adds a kit to the shopping cart. Validates that the kit exists and that there is
     * sufficient stock before adding it to the cart. Updates the kit to reflect the new stock level.
     * 
     * @param id the ID of the kit to add
     * @param quantity the quantity of the kit to add
     * @return ResponseEntity representing the result of the operation (OK, BAD_REQUEST, NOT_FOUND, INTERNAL_SERVER_ERROR)
     */
    @PostMapping("/add/{userId}/{id}/{quantity}")
    public ResponseEntity<Void> addToCart(@PathVariable Integer userId, @PathVariable int id, @PathVariable int quantity) {
        LOG.info(String.format("POST /cart/add/%d/%d/?quantity=%d", userId, id, quantity));
        try {
            Kit kit = kitDao.getKit(id);
            ShoppingCart shoppingCart = getShoppingCartForUser(userId);
            if (kit != null) {
                if (kit.getQuantity() > 0) {
                    int prevQuantity = kit.getQuantity();
                    if ((prevQuantity - quantity) < 0) {
                        shoppingCart.addKit(kit, prevQuantity);
                        kit.setQuantity(0);
                    } else {
                        shoppingCart.addKit(kit, quantity);
                        kit.setQuantity(prevQuantity - quantity);
                    }
                    kitDao.updateKit(kit);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } 
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
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
    @DeleteMapping("/remove/{userId}/{id}/{quantity}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Integer userId, @PathVariable int id, @PathVariable int quantity) {
        LOG.info(String.format("DELETE /cart/remove/%d/%d/?quantity=", userId, id, quantity));
        try {
            Kit kit = kitDao.getKit(id);
            ShoppingCart shoppingCart = getShoppingCartForUser(userId);
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
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
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
    @GetMapping("/{userId}")
    public ResponseEntity<ArrayList<ShoppingCartKit>> getCartKits(@PathVariable Integer userId) throws IOException {
        LOG.info("GET /cart/" + userId);
        ShoppingCart shoppingCart = getShoppingCartForUser(userId);
        Map<Kit, Integer> cartItems = shoppingCart.getKits();

        ArrayList<ShoppingCartKit> shoppingCartItems = new ArrayList<>();
        for (Map.Entry<Kit, Integer> entry : cartItems.entrySet()) {
            Kit kit = entry.getKey();
            shoppingCartItems.add(new ShoppingCartKit(kit.getId(), entry.getValue(), kit.getName(), kit.getPrice()));
        }

        return new ResponseEntity<>(shoppingCartItems, HttpStatus.OK);
    }

    /**
    * Calculates and returns the total cost of all kits currently in the shopping cart.
    * This endpoint allows the client to view the total cost of the items in the cart.
    * 
    * @return A ResponseEntity containing the total cost and the HTTP status code.
    * @throws IOException if an I/O error occurs during kit retrieval.
    */
    @GetMapping("/total/{userId}")
    public ResponseEntity<Float> getTotalCost(@PathVariable Integer userId) throws IOException {
        LOG.info("GET /cart/total/" + userId);
        ShoppingCart shoppingCart = getShoppingCartForUser(userId);
        float totalCost = shoppingCart.getTotalCost();
        return new ResponseEntity<>(totalCost, HttpStatus.OK);
    }
}

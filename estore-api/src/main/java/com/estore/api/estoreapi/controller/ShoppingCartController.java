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
    @PostMapping("/add/{id}/{quantity}")
    public ResponseEntity<Void> addtoCart(@PathVariable int id, @PathVariable int quantity) {
        LOG.info("POST /cart/add/id/?quantity="+quantity);
        try {
            Kit kit = kitDao.getKit(id);
            if (kit != null) {
                shoppingCart.addKit(kit, quantity);
                int prevQuantity = kit.getQuantity();
                kit.setQuantity(prevQuantity + quantity);
                kitDao.updateKit(kit);
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
     * Removes a kit from the shopping cart. Validates that the kit exists in the cart before
     * removing the specified quantity. Updates the kit to reflect the returned stock level.
     * 
     * @param id the ID of the kit to remove
     * @param quantity the quantity of the kit to remove
     * @return ResponseEntity representing the result of the operation (OK, NOT_FOUND, INTERNAL_SERVER_ERROR)
     */
    @DeleteMapping("/remove/{id}/{quantity}")
    public ResponseEntity<Void> removeFromCart(@PathVariable int id, @PathVariable int quantity) {
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
    public ResponseEntity<ArrayList<ShoppingCartKit>> getCartKits() throws IOException {
        LOG.info("GET /cart");
        Map<Kit, Integer> cartItems = shoppingCart.getKits();
        
        
        // get the kit from kitDAO and add to a list to be returned
        ArrayList<ShoppingCartKit> shoppingCartItems = new ArrayList<>();

        for (Map.Entry<Kit, Integer> entry : cartItems.entrySet()) {
            Kit kit = entry.getKey();
            int quantity = entry.getValue();

            // Step 2: Extract the kit ID
            int kitId = kit.getId();

            // getting the name
            String name = kit.getName();

            // Step 3: Create a new data structure pairing the ID with the quantity
            // and add it to the list
            shoppingCartItems.add(new ShoppingCartKit(kitId, quantity, name));
        }


        LOG.info(cartItems.toString());
        return new ResponseEntity<>(shoppingCartItems, HttpStatus.OK);
    }
}

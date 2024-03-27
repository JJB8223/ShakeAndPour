package com.estore.api.estoreapi.model;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an order a user has made in the past
 * Handles operations like adding and removing kits from an order,
 * checking if a provided user is the one who made the order,
 * and returning all kits in the order
 * 
 * @author Duncan French
 */
public class Order {

    @JsonProperty("user") private String user; // contains the username of the user who made the order
    @JsonProperty("kit_quantities") private Map<Kit, Integer> kits;  // maps a Kit to the number of that kit in the order

    /**
     * Construct a new Order made by a user containing certain kits and their quantities
     * @param user The username of the user who made the order
     * @param kits A map of each kit in the order and quantity of that kit that was ordered
     */
    public Order(@JsonProperty("user") String user, @JsonProperty("kit_quantities") Map<Kit, Integer> kits) {
        this.user = user;
        this.kits = kits;
    }

    /**
     * Adds the quantity of the provided kit to the kits map. If the kit doesn't exist yet, it is 
     * added to the order. If it already exists, the quantity is increased by the specified amount
     * 
     * @param kit The kit being added
     * @param quantity The number of the type of the kit being added
     */
    public void addKits(Kit kit, int quantity) {
        if (quantity <= 0) {
            return; // prevents adding a Kit with an invalid quantity that will then be in the map
        }
        kits.put(kit, kits.getOrDefault(kit, 0) + quantity);
    }

    /**
     * This method removes the provided quantity of a kit from an order. If this would result 
     * in there being none of a kit left in the order, it is removed from the order
     * 
     * @param kit The kit whose quantity is decreased
     * @param quantity The amount that the kit's quantity is decreased
     */
    public void removeKits(Kit kit, int quantity) {
        int currentQuantity = kits.getOrDefault(kit, 0);
        if (currentQuantity <= quantity) { // this would result in there being none of the kit left in the order
            kits.remove(kit);
        } else {
            kits.put(kit, currentQuantity - quantity);
        }
    }

    /**
     * This method removes the provided kit from the order
     * 
     * @param kit The kit that is removed from the order
     */
    public void clearKitFromOrder(Kit kit) {
        kits.remove(kit);
    }

    /**
     * Returns the quantity of the specified kit in the order. If it is not in the order, returns 0
     * 
     * @param kit The kit whose quantity is being obtained
     * @return The quantity of the specified kit in the order
     */
    public int getKitQuantity(Kit kit) {
        return kits.getOrDefault(kit, 0);
    } 

    /**
     * Returns whether the specified username is the user who purchased this order
     * 
     * @param user The username that is being checked to see if it purchased this order
     * @return A boolean indicating whether the specified user purchased this order
     */
    public boolean purchasedBy(String user) {
        return user.equals(this.user);
    }

    /**
     * This method returns all the kits and their quantities in this order
     * 
     * @return A map of all kits and their quantities in this order
     */
    public Map<Kit, Integer> getAllKits() {
        return kits;
    }

}

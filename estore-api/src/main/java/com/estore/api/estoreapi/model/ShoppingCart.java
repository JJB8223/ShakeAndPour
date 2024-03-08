package com.estore.api.estoreapi.model;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a shopping cart for the e-store API. 
 * This class handles operations such as adding kits to the cart, 
 * removing kits from the cart, calculating the total cost, 
 * and checking kit quantities.
 * 
 * @author David Dobbins dpd8504
 */
public class ShoppingCart {
    private Map<Kit, Integer> kits;
    
    /**
     * Constructs a new ShoppingCart instance. Initializes the internal
     * storage for kits added to the cart.
     */
    public ShoppingCart() {
        kits = new HashMap<>();
    }

    /**
     * Adds a specified quantity of a kit to the shopping cart. If the kit
     * already exists in the cart, the specified quantity is added to the existing quantity.
     * 
     * @param kit The kit to be added to the cart.
     * @param quantity The quantity of the kit to add.
     */
    public void addKit(Kit kit, int quantity) {
        kits.put(kit, kits.getOrDefault(kit, 0) + quantity);
    }

    /**
     * Removes a specified quantity of a kit from the shopping cart. If the quantity
     * after removal is more than zero, updates the quantity in the cart; otherwise, removes
     * the kit from the cart entirely.
     * 
     * @param kit The kit to be removed from the cart.
     * @param quantity The quantity of the kit to remove.
     */
    public void removeKit(Kit kit, int quantity) {
        int currentQuantity = kits.getOrDefault(kit, 0);
        int newQuantity = currentQuantity - quantity;
        if (newQuantity > 0) {
            kits.put(kit, newQuantity);
        } else {
            kits.remove(kit);
        }
    }

    /**
     * Calculates and returns the total cost of all kits in the shopping cart.
     * 
     * @return The total cost of all kits in the cart.
     */
    public float getTotalCost() {
        float totalCost = 0;
        for (Kit drink: kits.keySet()) {
            float price = drink.getPrice();
            int quantity = kits.get(drink);
            totalCost += (price * quantity);
        }
        return totalCost;
    }

    /**
     * Clears all kits from the shopping cart.
     */
    public void clearCart() {
        kits = new HashMap<>();
    }

    /**
     * Checks if the shopping cart contains a specified kit.
     * 
     * @param kit The kit to check for in the cart.
     * @return true if the cart contains the kit; false otherwise.
     */
    public boolean containsKit(Kit kit) {
        if (kits.get(kit.getId()) != null) {
            return false;
        }
        return true;
    }

    /**
     * Retrieves the quantity of a specified kit in the shopping cart.
     * 
     * @param kit The kit whose quantity is to be retrieved.
     * @return The quantity of the specified kit in the cart; returns 0 if the kit is not in the cart.
     */
    public int getKitQuantity(Kit kit) {
        return kits.getOrDefault(kit, 0);
    }

    /**
     * Returns a map of all kits and their quantities in the shopping cart.
     * 
     * @return A map containing all kits in the cart along with their quantities.
     */
    public Map<Kit, Integer> getKits() {
        return kits;
    }


}

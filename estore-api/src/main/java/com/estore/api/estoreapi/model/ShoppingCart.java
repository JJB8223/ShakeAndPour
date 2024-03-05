package com.estore.api.estoreapi.model;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a shopping cart for the e-store API. 
 * This class handles operations such as adding products to the cart, 
 * removing products from the cart, calculating the total cost, 
 * and checking product quantities.
 * 
 * @author David Dobbins dpd8504
 */
public class ShoppingCart {
    private Map<Product, Integer> products;
    
    /**
     * Constructs a new ShoppingCart instance. Initializes the internal
     * storage for products added to the cart.
     */
    public ShoppingCart() {
        products = new HashMap<>();
    }

    /**
     * Adds a specified quantity of a product to the shopping cart. If the product
     * already exists in the cart, the specified quantity is added to the existing quantity.
     * 
     * @param product The product to be added to the cart.
     * @param quantity The quantity of the product to add.
     */
    public void addProduct(Product product, int quantity) {
        products.put(product, products.getOrDefault(product, 0) + quantity);
    }

    /**
     * Removes a specified quantity of a product from the shopping cart. If the quantity
     * after removal is more than zero, updates the quantity in the cart; otherwise, removes
     * the product from the cart entirely.
     * 
     * @param product The product to be removed from the cart.
     * @param quantity The quantity of the product to remove.
     */
    public void removeProduct(Product product, int quantity) {
        int currentQuantity = products.getOrDefault(product, 0);
        int newQuantity = currentQuantity - quantity;
        if (newQuantity > 0) {
            products.put(product, newQuantity);
        } else {
            products.remove(product);
        }
    }

    /**
     * Calculates and returns the total cost of all products in the shopping cart.
     * 
     * @return The total cost of all products in the cart.
     */
    public float getTotalCost() {
        float totalCost = 0;
        for (Product drink: products.keySet()) {
            float price = drink.getPrice();
            int quantity = products.get(drink);
            totalCost += (price * quantity);
        }
        return totalCost;
    }

    /**
     * Clears all products from the shopping cart.
     */
    public void clearCart() {
        products = new HashMap<>();
    }

    /**
     * Checks if the shopping cart contains a specified product.
     * 
     * @param product The product to check for in the cart.
     * @return true if the cart contains the product; false otherwise.
     */
    public boolean containsProduct(Product product) {
        if (products.get(product) == 0 || products.get(product) == null) {
            return false;
        }
        return true;
    }

    /**
     * Retrieves the quantity of a specified product in the shopping cart.
     * 
     * @param product The product whose quantity is to be retrieved.
     * @return The quantity of the specified product in the cart; returns 0 if the product is not in the cart.
     */
    public int getProductQuantity(Product product) {
        return products.getOrDefault(product, 0);
    }

    /**
     * Returns a map of all products and their quantities in the shopping cart.
     * 
     * @return A map containing all products in the cart along with their quantities.
     */
    public Map<Product, Integer> getProducts() {
        return products;
    }


}

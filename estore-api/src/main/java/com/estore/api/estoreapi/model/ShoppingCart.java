package com.estore.api.estoreapi.model;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private Map<Product, Integer> drinks;
    
    public ShoppingCart() {
        drinks = new HashMap<>();
    }

    public void addProduct(Product product, int quantity) {
        drinks.put(product, drinks.getOrDefault(product, 0) + quantity);
    }

    public void removeProduct(Product product, int quantity) {
        int currentQuantity = drinks.getOrDefault(product, 0);
        int newQuantity = currentQuantity - quantity;
        if (newQuantity > 0) {
            drinks.put(product, newQuantity);
        } else {
            drinks.remove(product);
        }
    }

    public float getTotalCost() {
        float totalCost = 0;
        for (Product drink: drinks.keySet()) {
            float price = drink.getPrice();
            int quantity = drinks.get(drink);
            totalCost += (price * quantity);
        }
        return totalCost;
    }

    public void clearCart() {
        drinks = new HashMap<>();
    }

    public Map<Product, Integer> getDrinks() {
        return drinks;
    }


}

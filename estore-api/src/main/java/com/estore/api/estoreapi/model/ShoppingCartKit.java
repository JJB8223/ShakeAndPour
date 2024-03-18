package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a shopping cart kit for the e-store API.
 *
 * @author Akhil Devarapalli
 */
public class ShoppingCartKit {
    @JsonProperty("id") private int kitID;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("name") private String name;
    @JsonProperty("price") private float price;

    /**
     * Create a new instance of a shopping cart kit
     * @param kitID the id of the new shopping cart kit
     * @param quantity the quantity of the shopping cart kit
     * @param name the name of the shopping cart kit
     * @param price the price of the shopping cart kit
     */
    public ShoppingCartKit(@JsonProperty("id") int kitID, @JsonProperty("quantity") int quantity, @JsonProperty("name") String name, @JsonProperty("price") float price) {
        this.kitID = kitID;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    /**
     * Get hte id of the shopping cart kit
     * @return the shopping cart kit's id
     */
    public int getID() {
        return this.kitID;
    }

    /**
     * Get the quantity of the shopping cart kit
     * @return the shopping cart kit's quantity
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Get the name of the shopping cart kit
     * @return the shopping cart kit's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the price of the shopping cart kit
     * @return the shopping cart kit's price
     */
    public float getPrice() {return this.price;}

    /**
     * Calculate the total price of the shopping cart kit with how many in stock
     * @return the value of the shopping kart kit's quantity * price
     */
    public float totalPrice() {
        return (float) this.quantity * this.price;
    }

}

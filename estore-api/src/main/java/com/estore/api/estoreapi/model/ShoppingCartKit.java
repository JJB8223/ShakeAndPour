package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShoppingCartKit {
    @JsonProperty("id") private int kitID;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("name") private String name;
    @JsonProperty("price") private float price;

    public ShoppingCartKit(@JsonProperty("id") int kitID, @JsonProperty("quantity") int quantity, @JsonProperty("name") String name, @JsonProperty("price") float price) {
        this.kitID = kitID;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    public int getID() {
        return this.kitID;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getName() {
        return this.name;
    }

    public float totalPrice() {
        return (float) this.quantity * this.price;
    }

}

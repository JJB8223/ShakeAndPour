package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShoppingCartKit {
    @JsonProperty("id") private int kitID;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("name") private String name;
    
    public ShoppingCartKit(@JsonProperty("id") int kitID, @JsonProperty("quantity") int quantity, @JsonProperty("name") String name) {
        this.kitID = kitID;
        this.quantity = quantity;
        this.name = name;
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

}

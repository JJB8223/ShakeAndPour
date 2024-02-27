package com.estore.api.estoreapi.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;;


public class kit {
    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;

    @JsonProperty("price") private float price;

    @JsonProperty("quantity") private int quantity;

    @JsonProperty("product_in_kit") private ArrayList<Integer> productsInKit;

    public kit(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("price") float price, 
                    @JsonProperty("quantity") int quantity,
                    @JsonProperty("product_in_kit") ArrayList<Integer> productsInKit) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.productsInKit = productsInKit;
    }

    public int getId() {return id;}

    public void setName(String name) {this.name = name;}

    public String getName() {return this.name;}

    public float getPrice() {return price;}

    public void setQuantity(int quantity){this.quantity = quantity;}

    public int getQuantity() {return quantity;}

    public ArrayList<Integer> getProductsInKit() {return productsInKit;}

    @Override
    public String toString() {
        return Integer.toString(id) + name + String.valueOf(price) + productsInKit.toString();
    }
}

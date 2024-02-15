package com.estore.api.estoreapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a Product entity
 * 
 * @author SWEN Faculty
 */
public class Product {
    private static final Logger LOG = Logger.getLogger(Product.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Product [id=%d, name=%s price=%.2f quantity=%d]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;

    @JsonProperty("price") private float price;

    @JsonProperty("quantity") private int quantity;

    /**
     * Create a Product with the given id, name, price, and quantity
     * @param id The id of the Product
     * @param name The name of the Product
     * @param price the initial price of the Product
     * @param quantity the initial quantity of the Product
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Product(@JsonProperty("id") int id, @JsonProperty("name") String name,
                   @JsonProperty("price") float price, @JsonProperty("quantity") int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Retrieves the id of the Product
     * @return The id of the Product
     */
    public int getId() {return id;}

    /**
     * Sets the name of the Product - necessary for JSON object to Java object deserialization
     * @param name The name of the Product
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the Product
     * @return The name of the Product
     */
    public String getName() {return name;}

    /**
     * Update the price of the Product
     * @param price the updated Price
     */
    public void setPrice(float price) {this.price = price;}

    /**
     * Retrieve the price of the Product
     * @return The price of the Product
     */
    public float getPrice() {return price;}

    /**
     * Update the quantity of the Product
     * @param quantity the new quantity of the Product
     */
    public void setQuantity(int quantity){this.quantity = quantity;}

    /**
     * Retrieve the quantity/amount of the Product
     * @return the quantity of the Product
     */
    public int getQuantity() {return quantity;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name, price, quantity);
    }
}
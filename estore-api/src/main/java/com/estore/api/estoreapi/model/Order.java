package com.estore.api.estoreapi.model;
import java.util.ArrayList;

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

    @JsonProperty("id") private int id; // unique identifier for an order
    @JsonProperty("user") private String user; // contains the username of the user who made the order
    @JsonProperty("kits_in_order") private ArrayList<Kit> kits; // contains all the kits in the order

    /**
     * Construct a new Order made by a user containing certain kits and their quantities
     * @param user The username of the user who made the order
     * @param kits An array of all the kits in the order
     */
    public Order(@JsonProperty("id") int id, @JsonProperty("user") String user, @JsonProperty("kit_quantities") ArrayList<Kit> kits) {
        this.id = id;
        this.user = user;
        this.kits = kits;
    }

    /**
     * This method adds the provided kit to the order
     * 
     * @param kit The kit being added to the order
     */
    public void addKit(Kit kit) {
        for (Kit k : kits) {
            if (k.getName().equals(kit.getName())) {
                return;
            }
        } // if we reach the end of the loop then the kit is not in the kits array
        kits.add(kit);
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
     * Returns whether the specified username is the user who purchased this order
     * 
     * @param user The username that is being checked to see if it purchased this order
     * @return A boolean indicating whether the specified user purchased this order
     */
    public boolean purchasedBy(String user) {
        return user.equals(this.user);
    }

    /**
     * This method returns the username of the user who purchased this order
     * 
     * @return A string containing the username of the user who purchased this order
     */
    public String getUser(){
        return user;
    }

    /**
     * This method returns all the kits in this order
     * 
     * @return A list of all kits in this order
     */
    public ArrayList<Kit> getAllKits() {
        return kits;
    }

    /**
     * This method returns the order's ID
     * 
     * @return an int containing this order's ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * This method returns whether a specified piece of text matches the names of any kits in the order
     * 
     * @param text A String of searched text
     * @return Whether the specified string was found in any of the kits
     */
    public boolean containsMatchingKit(String text) {
        // if the name of any kits in the order match the string, this will be true
        boolean hasMatchingKit = false; 

        for (Kit k : kits) {
            String lowercaseName = k.getName().toLowerCase();
            if (lowercaseName.contains(text.toLowerCase())) {
                hasMatchingKit = true; 
            }
        }

        return hasMatchingKit;
    }

    /**
     * This method returns the Order in string format
     * 
     * @return A string version of the Order
     */
    public String toString() {
        return Integer.toString(id) + user + kits.toString();
    }

}

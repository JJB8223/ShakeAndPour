package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.logging.Logger;

/**
 * Represents a User entity of the e-store
 *
 * @author Matthew Morrison msm8275
 * @author Joshua Bay jjb8223
 */
public class User {
    private static final Logger LOG = Logger.getLogger(User.class.getName());

    @JsonProperty("id") private int id;
    @JsonProperty("username") private String username;

    @JsonProperty("password") private String password;

    @JsonProperty("name") private String name;

    @JsonProperty("role") private UserRole role;

    /**
     * Create a new user with a given id, username, password, name and role
     * @param id the id of the new User
     * @param username the username of the new User
     * @param password the password of the new User
     * @param name the name of the new User
     * @param role the role of the new User
     */
    public User(@JsonProperty("id") int id,
                @JsonProperty("username") String username,
                @JsonProperty("password") String password,
                @JsonProperty("name") String name,
                @JsonProperty("role") UserRole role){
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    /**
     * ENUM values to distinguish the role a user plays
     * A user can either be the admin or a standard customer
     */
    public enum UserRole {
        CUSTOMER,
        ADMIN;

        /**
         * Get the role in lowercase, used for JSON objects
         * @return the role string in lowercase
         */
        public String getRole() {
            return this.name().toLowerCase();
        }
    }

    /**
     * Get the id of the user
     * @return the user's id
     */
    public int getId(){return id;}

    /**
     * Get the user's username
     * @return the user's username
     */
    public String getUsername() {return username;}

    /**
     * Update the user's username to a new string
     * @param username the updated username
     */
    public void setUsername(String username) {this.username = username;}

    /**
     * Get the user's password
     * @return the user's password
     */
    public String getPassword() {return password;}

    /**
     * Update the user's password to a new string
     * @param password the updated password
     */
    public void setPassword(String password) {this.password = password;}

    /**
     * get the user's name
     * @return the user's name
     */
    public String getName(){return name;}

    /**
     * Update the user's name to a new string
     * @param name the updated name
     */
    public void setName(String name){this.name = name;}


    /**
     * Get the user's role
     * @return the user's role
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Update the user's role
     * @param role the new role
     */
    public void setRole(UserRole role) {this.role = role;}

}

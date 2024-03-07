package com.estore.api.estoreapi.model;


import com.fasterxml.jackson.annotation.JsonCreator;
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

    public enum UserRole {
        CUSTOMER,
        ADMIN
    }

    public int getId(){return id;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public UserRole getRole() {return role;}

    public void setRole(UserRole role) {this.role = role;}

}

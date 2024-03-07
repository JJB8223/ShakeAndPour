package com.estore.api.estoreapi.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.model.User.UserRole;

/**
 * Controller for handling anything that would need to be done with users within the e-store API.
 * This class provides endpoints for registering, getting, updating. deleting and intializing Users.
 * 
 * @author Joshua Bay jjb8223
 */
@RestController
@RequestMapping("users")
public class UserController {

    private static final Logger LOG = Logger.getLogger(UserController.class.getName());

    private static Map<Integer, User> users = new HashMap<>();
    private static int nextId = 0;



    /**
     * Registers a new user.
     * 
     * @param user The user to register.
     * @return ResponseEntity indicating the status of the registration operation.
     */
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody User user) {
        LOG.info("POST users/register");
        user.setId(nextId++);
        users.put(user.getId(), user);
        LoginController.addCredentials(user.getUsername(), user.getPassword());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Retrieves a user by ID.
     * 
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity containing the retrieved user or a status indicating not found.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) { 
        LOG.info("GET /users/get/" + id);
        User result = users.get(id);
        if(result == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
        return new ResponseEntity<>(result,HttpStatus.OK);
        }
    }

    /**
     * Updates an existing user.
     * 
     * @param id The ID of the user to update.
     * @param user The updated user object.
     * @return ResponseEntity indicating the status of the update operation.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable int id, @RequestBody User user) {
        LOG.info("PUT /users/update/" + id);
        if(users.containsKey(id)){
            user.setId(id);
            users.put(id,user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes a user by ID.
     * 
     * @param id The ID of the user to delete.
     * @return ResponseEntity indicating the status of the delete operation.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        LOG.info("DELETE /users/delete/" + id);
        if(users.containsKey(id)){
            users.remove(id);
            User user = users.get(id);
            LoginController.delCredentials(user.getUsername(), user.getPassword());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }    

    /**
     * Initializes some users for testing purposes.
     * 
     * @return ResponseEntity indicating the status of the initialization operation.
     */
    @PostMapping("/init-users")
    public ResponseEntity<Void> initUser(){
        LOG.info("POST /users/init-users");
        User Akhil = new User(nextId, "customer", "password", "Akhil", UserRole.CUSTOMER);
        users.put(nextId, Akhil);
        nextId++;
        User MattyMo = new User(2, "admin", "password", "MattyMo", UserRole.ADMIN);
        users.put(nextId, MattyMo);
        nextId++;
        
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

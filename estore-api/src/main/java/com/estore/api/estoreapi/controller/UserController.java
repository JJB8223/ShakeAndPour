package com.estore.api.estoreapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.estore.api.estoreapi.persistence.UserDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.estore.api.estoreapi.model.User;

/**
 * Controller for handling anything that would need to be done with users within the e-store API.
 * This class provides endpoints for registering, getting, updating. deleting and initializing Users.
 * Also helps user's to log in using their credentials
 *
 * @author Joshua Bay jjb8223
 * @author Matthew Morrison msm8275
 */
@RestController
@RequestMapping("users")
public class UserController {

    private static final Logger LOG = Logger.getLogger(UserController.class.getName());

    private UserDAO userDAO;

    /**
     * Creates a REST API controller to reponds to requests
     * @param userDao The {@link UserDAO Product Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public UserController(UserDAO userDao){
        this.userDAO = userDao;
    }


    /**
     * Registers a new user.
     * 
     * @param user The user to register.
     * @return ResponseEntity indicating the status of the registration operation.
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        LOG.info("POST users/register " + user);
        try {
            User createdU = userDAO.createUser(user);
            if(createdU == null){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(createdU, HttpStatus.CREATED);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a user by ID.
     * 
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity containing the retrieved user or a status indicating not found.
     */
    @GetMapping("/getById/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) { 
        LOG.info("GET /users/getById/" + id);

        try {
            User u = userDAO.getUser(id);
            if (u != null){
                return new ResponseEntity<>(u, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getByUsername/{username}")
    public ResponseEntity<User> getUserByName(@PathVariable String username) {
        LOG.info("GET /users/getByUsername/" + username);

        User[] users = userDAO.getUsers();
        User foundUser = null;
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                foundUser = user;
            }
        }
        if (foundUser != null) {
            return new ResponseEntity<>(foundUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Update an existing {@linkplain User User} username
     * @param id the id of the existing {@linkplain User User}
     * @param username the new username
     * @return ResponseEntity indicating the status of the operation.
     * @throws IOException if an internal error occurs
     */
    @PutMapping("/update/{id}/u")
    public ResponseEntity<User> updateUsername(@PathVariable int id, @RequestParam String username)
        throws IOException {
        LOG.info("PUT /users/update/" + id + "/u?username=" + username);
        try{
            User currU = userDAO.getUser(id);
            if (currU == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            User preexisting = null;
            User[] users = userDAO.getUsers();
            for (User user: users) {
                if (user.getUsername().equals(username)) {
                    preexisting = user;
                }
            }
            if (preexisting != null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            User status = userDAO.updateUsername(currU, username);

            if (status == null){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            else{
                return new ResponseEntity<>(status, HttpStatus.OK);
            }
        }
        catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update an existing {@linkplain User User} password
     * @param id the id of the existing {@linkplain User User}
     * @param password the new password
     * @return ResponseEntity indicating the status of the operation.
     * @throws IOException if an internal error occurs
     */
    @PutMapping("/update/{id}/p")
    public ResponseEntity<User> updatePassword(@PathVariable int id, @RequestParam String password)
            throws IOException {
        LOG.info("PUT /users/update/" + id + "/p?password=" + password);
        try{
            User currU = userDAO.getUser(id);
            if (currU == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            User status = userDAO.updatePassword(currU, password);

            if (status == null){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            else{
                return new ResponseEntity<>(status, HttpStatus.OK);
            }
        }
        catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update an existing {@linkplain User User} name
     * @param id the id of the existing {@linkplain User User}
     * @param name the new name
     * @return ResponseEntity indicating the status of the operation.
     * @throws IOException if an internal error occurs
     */
    @PutMapping("/update/{id}/n")
    public ResponseEntity<User> updateName(@PathVariable int id, @RequestParam String name)
            throws IOException {
        LOG.info("PUT /users/update/" + id + "/n?name=" + name);
        try{
            User currU = userDAO.getUser(id);
            if (currU == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            User status = userDAO.updateName(currU, name);

            if (status == null){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            else{
                return new ResponseEntity<>(status, HttpStatus.OK);
            }
        }
        catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
        try {
            boolean status = userDAO.deleteUser(id);
            if (!status) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Login a user given their credentials
     * @param username the username of the user to login
     * @param password the password of the user to login
     * @return A response entity with a string signifying if login was
     * successful or not.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestParam String username, @RequestParam String password) {
        LOG.info("POST /users/login?username=" + username);
        
        User foundUser = null;
        User[] users = userDAO.getUsers();
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                foundUser = user;
            }
        }
        
        if (foundUser != null && userDAO.authorize(username, password)) {
            String userType = username.equals("admin") ? "admin" : "user";
            LoginResponse response = new LoginResponse(foundUser.getId(), userType, userType + " login successful");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(0, "none", "Invalid username or password"));
        }
    }

    public class LoginResponse {
        private final int userId;
        private final String userType;
        private final String message;
    
        public LoginResponse(int userId, String userType, String message) {
            this.userId = userId;
            this.userType = userType;
            this.message = message;
        }
    
        // Getters
        public long getUserId() {
            return userId;
        }
    
        public String getUserType() {
            return userType;
        }
    
        public String getMessage() {
            return message;
        }
    }
}

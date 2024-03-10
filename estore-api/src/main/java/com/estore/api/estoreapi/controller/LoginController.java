package com.estore.api.estoreapi.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.UserDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for handling anything that would need to be done with users within the e-store API.
 * This class provides endpoints authenticating users.
 * It also contains two functions to add and delete users from the authorized list.
 * 
 * @author Joshua Bay jjb8223
 */
@RestController
@RequestMapping("login")
public class LoginController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());

    private static Map<String, String> credentials = new HashMap<>();

    private UserDAO userDAO;


    public void getCredentials() {
        User[] users = userDAO.getUsers();
        for(User u : users){
            String username = u.getUsername();
            String password = u.getPassword();
            credentials.put(username, password);
        }
    }

    /**
     * Creates a REST API controller to reponds to requests
     * @param userDao The {@link UserDAO Product Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public LoginController(UserDAO userDao){
        this.userDAO = userDao;
        getCredentials();
    }

    /**
     * Adds a user to the credentials list
     * @param username the username of the new user
     * @param password the password of the new user
     */
    public static void addCredentials(String username, String password) {
        credentials.put(username, password);
    }

    /**
     * Deletes a user to the credentials list
     * @param username the username of the new user
     * @param password the password of the new user
     */
    public static void delCredentials(String username, String password) {
        credentials.remove(username, password);
    }

    /**
     * Authenticates an Username and password by checking if they are in the
     * authorized credentials
     * @param username the username passed through by the user
     * @param password the password passsed through by the user
     * @return Returns either an Ok or and Unathorized depending on whether
     * their login was valid or invalid respectively
     */
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestParam String username, @RequestParam String password) { 
        LOG.info("POST /login/authenticate");
        if(credentials.containsKey(username) && credentials.get(username).equals(password)){
            return new ResponseEntity<>("Login Successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }
}

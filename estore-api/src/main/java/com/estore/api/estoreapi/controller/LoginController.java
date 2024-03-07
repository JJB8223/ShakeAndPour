package com.estore.api.estoreapi.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class LoginController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());

    private static Map<String, String> credentials = new HashMap<>();

    public static void addCredentials(String username, String password) {
        credentials.put(username, password);
    }

    public static void delCredentials(String username, String password) {
        credentials.remove(username, password);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestParam String username, @RequestParam String password) { 
        LOG.info("POST /login/authenticate");
        if(credentials.containsKey(username) && credentials.get(username).equals(password)){
            return new ResponseEntity<>("Login Sucessful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }
}

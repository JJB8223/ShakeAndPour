package com.estore.api.estoreapi.controller;


import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test the User Controller class
 *
 * @author Matthew Morrison msm8275
 */
@Tag("Controller-tier")
public class UserControllerTest {
    private UserDAO mockUserDao;
    private UserController UserController;

    @BeforeEach
    public void setUpUserController(){
        mockUserDao = mock(UserDAO.class);
        UserController = new UserController((mockUserDao));
    }

    @Test
    public void testCreateUserSuccess() throws IOException{
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);

        when(mockUserDao.createUser(u)).thenReturn(u);


        ResponseEntity<User> r = UserController.registerUser(u);
        // analyze
        assertEquals(HttpStatus.CREATED,r.getStatusCode());
        assertEquals(u,r.getBody());

    }

    @Test
    public void testCreateKitFail() throws IOException {
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);

        when(mockUserDao.createUser(u)).thenReturn(null);

        ResponseEntity<User> r = UserController.registerUser(u);
        // analyze
        assertEquals(HttpStatus.CONFLICT,r.getStatusCode());
    }

    @Test
    public void testCreateKitException() throws IOException{
        doThrow(new IOException()).when(mockUserDao).createUser(null);

        ResponseEntity<User> r = UserController.registerUser(null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, r.getStatusCode());
    }

    @Test
    public void testGetUserSuccess() throws IOException {
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);

        when(mockUserDao.getUser(u.getId())).thenReturn(u);

        ResponseEntity<User> r = UserController.getUser(id);

        // Analyze
        assertEquals(HttpStatus.OK,r.getStatusCode());
        assertEquals(u,r.getBody());
    }

    @Test
    public void testGetUserNotFound() throws IOException {
        int uID = 900;

        when(mockUserDao.getUser(uID)).thenReturn(null);

        ResponseEntity<User> r = UserController.getUser(uID);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,r.getStatusCode());
    }

    @Test
    public void testGetUserException() throws IOException {
        int uID = 900;

        doThrow(new IOException()).when(mockUserDao).getUser(uID);

        ResponseEntity<User> r = UserController.getUser(uID);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,r.getStatusCode());
    }

    @Test
    public void testUpdateUsernameSuccess() throws IOException {
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);

        when(mockUserDao.createUser(u)).thenReturn(u);
        ResponseEntity<User> r = UserController.registerUser(u);
        // analyze
        assertEquals(HttpStatus.CREATED,r.getStatusCode());

        String newUsername = "newUser";

        when(mockUserDao.updateUsername(u, newUsername)).thenReturn(u);
        when(mockUserDao.getUser(u.getId())).thenReturn(u);
        ResponseEntity<Void> s = UserController.updateUsername(u.getId(), newUsername);

        assertEquals(HttpStatus.OK,s.getStatusCode());

        assertEquals(newUsername, u.getUsername());


    }
}

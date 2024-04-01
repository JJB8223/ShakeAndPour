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
import static org.junit.jupiter.api.Assertions.assertNull;
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

    private User[] testUsers;

    @BeforeEach
    public void setUpUserController(){
        mockUserDao = mock(UserDAO.class);
        UserController = new UserController((mockUserDao));

        testUsers = new User[2];
        User admin = new User(100, "admin", "1234",
                "admin", User.UserRole.ADMIN);
        User testCustomer = new User(101, "test", "0000",
                "John Doe", User.UserRole.CUSTOMER);

        testUsers[0] = admin;
        testUsers[1] = testCustomer;
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

        when(mockUserDao.createUser(username, password, name)).thenReturn(u);
        when(mockUserDao.getUsers()).thenReturn(testUsers);


        ResponseEntity<User> r = UserController.registerUser(username, password, name);
        // analyze
        assertEquals(HttpStatus.CREATED,r.getStatusCode());
        assertEquals(u,r.getBody());

    }

    @Test
    public void testCreateUserFail() throws IOException {
        // Expected values
        String username = "testUser";
        String password = "000000";
        String name = "New User";


        when(mockUserDao.createUser(username, password, name)).thenReturn(null);
        when(mockUserDao.getUsers()).thenReturn(testUsers);

        ResponseEntity<User> r = UserController.registerUser(username, password, name);
        // analyze
        assertEquals(HttpStatus.CONFLICT,r.getStatusCode());
    }

    @Test
    public void testCreateUserUsernameFound() throws IOException {
        // Expected values
        int id = 102;
        String username = "test";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);

        when(mockUserDao.createUser(username, password, name)).thenReturn(u);
        when(mockUserDao.getUsers()).thenReturn(testUsers);

        ResponseEntity<User> r = UserController.registerUser(username, password, name);
        // analyze
        assertEquals(HttpStatus.BAD_REQUEST,r.getStatusCode());
    }

    @Test
    public void testCreateUserException() throws IOException{
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);
        doThrow(new IOException()).when(mockUserDao).createUser(username, password, name);
        when(mockUserDao.getUsers()).thenReturn(testUsers);

        ResponseEntity<User> r = UserController.registerUser(username, password, name);

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

        when(mockUserDao.createUser(username, password, name)).thenReturn(u);
        when(mockUserDao.getUsers()).thenReturn(testUsers);


        ResponseEntity<User> r = UserController.registerUser(username, password, name);
        // analyze
        assertEquals(HttpStatus.CREATED,r.getStatusCode());

        // update username
        String newUsername = "newUser";

        User updated = new User(id, newUsername, password, name, role);

        when(mockUserDao.getUser(u.getId())).thenReturn(u);
        when(mockUserDao.updateUsername(u, newUsername)).thenReturn(updated);
        ResponseEntity<User> s = UserController.updateUsername(u.getId(), newUsername);
        assertEquals(HttpStatus.OK,s.getStatusCode());


        assertEquals(updated,s.getBody());


    }

    @Test
    public void testUpdateUsernameNotFound() throws IOException {
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);

        when(mockUserDao.createUser(username, password, name)).thenReturn(u);
        when(mockUserDao.getUsers()).thenReturn(testUsers);


        ResponseEntity<User> r = UserController.registerUser(username, password, name);
        // analyze
        assertEquals(HttpStatus.CREATED,r.getStatusCode());

        // update username
        String newUsername = "newUser";

        // cannot find user
        when(mockUserDao.getUser(u.getId())).thenReturn(null);
        ResponseEntity<User> s = UserController.updateUsername(200, newUsername);
        assertEquals(HttpStatus.NOT_FOUND,s.getStatusCode());

        assertNull(s.getBody());
    }

    @Test
    public void testUpdateUsernameFail() throws IOException {
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);

        when(mockUserDao.createUser(username, password, name)).thenReturn(u);
        when(mockUserDao.getUsers()).thenReturn(testUsers);


        ResponseEntity<User> r = UserController.registerUser(username, password, name);
        // analyze
        assertEquals(HttpStatus.CREATED,r.getStatusCode());

        // update username
        String newUsername = "newUser";

        // cannot find user
        when(mockUserDao.getUser(u.getId())).thenReturn(u);
        when(mockUserDao.updateUsername(u, newUsername)).thenReturn(null);
        ResponseEntity<User> s = UserController.updateUsername(102, newUsername);
        assertEquals(HttpStatus.CONFLICT,s.getStatusCode());

        assertNull(s.getBody());
    }

    @Test
    public void testUpdateUsernameException() throws IOException {
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);
        when(mockUserDao.createUser(username, password, name)).thenReturn(u);
        when(mockUserDao.getUsers()).thenReturn(testUsers);


        ResponseEntity<User> r = UserController.registerUser(username, password, name);
        // analyze
        assertEquals(HttpStatus.CREATED,r.getStatusCode());

        // update username
        String newUsername = "newUser";

        when(mockUserDao.getUser(u.getId())).thenReturn(u);
        doThrow(new IOException()).when(mockUserDao).updateUsername(u, newUsername);
        ResponseEntity<User> s = UserController.updateUsername(id, newUsername);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, s.getStatusCode());
    }

    @Test
    public void testUpdatePasswordSuccess() throws IOException {
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);

        when(mockUserDao.createUser(username, password, name)).thenReturn(u);
        when(mockUserDao.getUsers()).thenReturn(testUsers);


        ResponseEntity<User> r = UserController.registerUser(username, password, name);
        // analyze
        assertEquals(HttpStatus.CREATED,r.getStatusCode());

        // update password
        String newPass = "1123";

        User updated = new User(id, username, newPass, name, role);

        when(mockUserDao.getUser(u.getId())).thenReturn(u);
        when(mockUserDao.updatePassword(u, newPass)).thenReturn(updated);
        ResponseEntity<User> s = UserController.updatePassword(u.getId(), newPass);
        assertEquals(HttpStatus.OK,s.getStatusCode());


        assertEquals(updated,s.getBody());


    }

    @Test
    public void testUpdatePasswordNotFound() throws IOException {
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);

        when(mockUserDao.createUser(username, password, name)).thenReturn(u);
        when(mockUserDao.getUsers()).thenReturn(testUsers);


        ResponseEntity<User> r = UserController.registerUser(username, password, name);
        // analyze
        assertEquals(HttpStatus.CREATED,r.getStatusCode());

        // update password
        String newPass = "1123";

        // cannot find user
        when(mockUserDao.getUser(u.getId())).thenReturn(null);
        ResponseEntity<User> s = UserController.updatePassword(200, newPass);
        assertEquals(HttpStatus.NOT_FOUND,s.getStatusCode());

        assertNull(s.getBody());
    }

    @Test
    public void testUpdatePasswordFail() throws IOException {
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);

        when(mockUserDao.createUser(username, password, name)).thenReturn(u);
        when(mockUserDao.getUsers()).thenReturn(testUsers);


        ResponseEntity<User> r = UserController.registerUser(username, password, name);
        // analyze
        assertEquals(HttpStatus.CREATED,r.getStatusCode());

        // update password
        String newPass = "1123";

        // cannot find user
        when(mockUserDao.getUser(u.getId())).thenReturn(u);
        when(mockUserDao.updatePassword(u, newPass)).thenReturn(null);
        ResponseEntity<User> s = UserController.updatePassword(102, newPass);
        assertEquals(HttpStatus.CONFLICT,s.getStatusCode());

        assertNull(s.getBody());
    }

    @Test
    public void testUpdatePasswordException() throws IOException {
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);

        when(mockUserDao.createUser(username, password, name)).thenReturn(u);
        when(mockUserDao.getUsers()).thenReturn(testUsers);


        ResponseEntity<User> r = UserController.registerUser(username, password, name);
        // analyze
        assertEquals(HttpStatus.CREATED,r.getStatusCode());

        // update password
        String newPass = "1123";

        when(mockUserDao.getUser(u.getId())).thenReturn(u);
        doThrow(new IOException()).when(mockUserDao).updatePassword(u, newPass);
        ResponseEntity<User> s = UserController.updatePassword(id, newPass);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, s.getStatusCode());
    }

    @Test
    public void testUpdateNameSuccess() throws IOException {
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);

        when(mockUserDao.createUser(username, password, name)).thenReturn(u);
        when(mockUserDao.getUsers()).thenReturn(testUsers);


        ResponseEntity<User> r = UserController.registerUser(username, password, name);
        // analyze
        assertEquals(HttpStatus.CREATED,r.getStatusCode());

        // update name
        String newName = "John Doe";

        User updated = new User(id, username, password, newName, role);

        when(mockUserDao.getUser(u.getId())).thenReturn(u);
        when(mockUserDao.updateName(u, newName)).thenReturn(updated);
        ResponseEntity<User> s = UserController.updateName(u.getId(), newName);
        assertEquals(HttpStatus.OK,s.getStatusCode());


        assertEquals(updated,s.getBody());


    }

    @Test
    public void testUpdateNameNotFound() throws IOException {
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);

        when(mockUserDao.createUser(username, password, name)).thenReturn(u);
        when(mockUserDao.getUsers()).thenReturn(testUsers);


        ResponseEntity<User> r = UserController.registerUser(username, password, name);
        // analyze
        assertEquals(HttpStatus.CREATED,r.getStatusCode());

        // update name
        String newName = "John Doe";

        // cannot find user
        when(mockUserDao.getUser(u.getId())).thenReturn(null);
        ResponseEntity<User> s = UserController.updateName(200, newName);
        assertEquals(HttpStatus.NOT_FOUND,s.getStatusCode());

        assertNull(s.getBody());
    }

    @Test
    public void testUpdateNameFail() throws IOException {
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);

        when(mockUserDao.createUser(username, password, name)).thenReturn(u);
        when(mockUserDao.getUsers()).thenReturn(testUsers);


        ResponseEntity<User> r = UserController.registerUser(username, password, name);
        // analyze
        assertEquals(HttpStatus.CREATED,r.getStatusCode());

        // update name
        String newName = "John Doe";

        // cannot find user
        when(mockUserDao.getUser(u.getId())).thenReturn(u);
        when(mockUserDao.updateName(u, newName)).thenReturn(null);
        ResponseEntity<User> s = UserController.updateName(102, newName);
        assertEquals(HttpStatus.CONFLICT,s.getStatusCode());

        assertNull(s.getBody());
    }

    @Test
    public void testUpdateNameException() throws IOException {
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);

        when(mockUserDao.createUser(username, password, name)).thenReturn(u);
        when(mockUserDao.getUsers()).thenReturn(testUsers);


        ResponseEntity<User> r = UserController.registerUser(username, password, name);
        // analyze
        assertEquals(HttpStatus.CREATED,r.getStatusCode());

        // update name
        String newName = "John Doe";

        when(mockUserDao.getUser(u.getId())).thenReturn(u);
        doThrow(new IOException()).when(mockUserDao).updateName(u, newName);
        ResponseEntity<User> s = UserController.updateName(id, newName);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, s.getStatusCode());
    }

    @Test
    public void testDeleteUserSuccess() throws IOException{
        int userId = 100;

        when(mockUserDao.deleteUser(userId)).thenReturn(true);
        ResponseEntity<Void> response = UserController.deleteUser(userId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteUserNotFound() throws IOException{
        int userId = 100;

        when(mockUserDao.deleteUser(userId)).thenReturn(false);
        ResponseEntity<Void> response = UserController.deleteUser(userId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteUserException() throws IOException{
        int userId = 100;

        doThrow(new IOException()).when(mockUserDao).deleteUser(userId);
        ResponseEntity<Void> response = UserController.deleteUser(userId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());

    }

    @Test
    public void testLoginAdmin() {
        String username = "admin";
        String password = "000000";

        when(mockUserDao.authorize(username, password)).thenReturn(true);
        ResponseEntity<String> r = UserController.login(username, password);

        assertEquals(HttpStatus.OK,r.getStatusCode());
        assertEquals("admin login successful", r.getBody());
    }

    @Test
    public void testLoginAdminFail() {
        String username = "admin";
        String password = "000000";

        when(mockUserDao.authorize(username, password)).thenReturn(false);
        ResponseEntity<String> r = UserController.login(username, password);

        assertEquals(HttpStatus.UNAUTHORIZED,r.getStatusCode());
        assertEquals("Invalid username or password", r.getBody());
    }

    @Test
    public void testLoginUser() {
        // Expected values
        String username = "user";
        String password = "000000";

        when(mockUserDao.authorize(username, password)).thenReturn(true);
        ResponseEntity<String> r = UserController.login(username, password);

        assertEquals(HttpStatus.OK,r.getStatusCode());
        assertEquals("user login successful", r.getBody());
    }

    @Test
    public void testLoginFail() {
        // Expected values
        String username = "user";
        String password = "000000";

        when(mockUserDao.authorize("false", password)).thenReturn(false);
        ResponseEntity<String> r = UserController.login(username, password);

        assertEquals(HttpStatus.UNAUTHORIZED,r.getStatusCode());
        assertEquals("Invalid username or password", r.getBody());
    }
}

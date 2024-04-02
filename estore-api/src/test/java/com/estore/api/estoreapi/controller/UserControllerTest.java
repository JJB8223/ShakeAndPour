package com.estore.api.estoreapi.controller;


import com.estore.api.estoreapi.controller.UserController.LoginResponse;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    public void testGetUserByNameSuccess() {
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;
        User foundUser = new User(id, username, password, name, role);

        when(mockUserDao.getUsers()).thenReturn(new User[]{foundUser});

        ResponseEntity<User> response = UserController.getUserByName(username);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(foundUser, response.getBody());
    }

    @Test
    public void testGetUserByNameNotFound() {
        // Expected values
        String username = "nonExistingUser";

        when(mockUserDao.getUsers()).thenReturn(new User[]{});

        ResponseEntity<User> response = UserController.getUserByName(username);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdateUsernameSuccess() throws IOException {
        int id = 102;
        String originalUsername = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        // This represents the existing user before username update.
        User originalUser = new User(id, originalUsername, password, name, role);

        // Simulate that the user exists for the given ID.
        when(mockUserDao.getUser(id)).thenReturn(originalUser);

        // Simulate getUsers() to avoid null pointer exception by returning an empty array.
        when(mockUserDao.getUsers()).thenReturn(new User[]{});

        String newUsername = "newUser";

        // This represents the user after the username has been successfully updated.
        User updatedUser = new User(id, newUsername, password, name, role);

        // Simulate successful username update operation.
        when(mockUserDao.updateUsername(originalUser, newUsername)).thenReturn(updatedUser);

        // Attempt to update the username.
        ResponseEntity<User> response = UserController.updateUsername(id, newUsername);

        // Verify the operation was successful.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newUsername, response.getBody().getUsername());
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

        when(mockUserDao.createUser(u)).thenReturn(u);
        ResponseEntity<User> r = UserController.registerUser(u);
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
    public void testUpdateUsernameTaken() throws IOException {
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User existingUser = new User(id, username, password, name, role);

        // Simulate registering the user successfully
        when(mockUserDao.createUser(existingUser)).thenReturn(existingUser);
        ResponseEntity<User> registrationResponse = UserController.registerUser(existingUser);
        assertEquals(HttpStatus.CREATED, registrationResponse.getStatusCode());

        String newUsername = "newUser";
    
        // Setup to simulate that the user is found successfully
        when(mockUserDao.getUser(id)).thenReturn(existingUser);

        // Simulate that another user with the newUsername already exists
        User conflictingUser = new User(103, newUsername, "anotherPass", "Another User", User.UserRole.CUSTOMER);
        User[] users = new User[] { existingUser, conflictingUser };
        when(mockUserDao.getUsers()).thenReturn(users);
    
        // Execute update username
        ResponseEntity<User> updateResponse = UserController.updateUsername(id, newUsername);
    
        // Verify the response is HttpStatus.BAD_REQUEST due to username being in use
        assertEquals(HttpStatus.BAD_REQUEST, updateResponse.getStatusCode());
        assertNull(updateResponse.getBody());
    }

    @Test
    public void testUpdateUsernameConflict() throws IOException {   
        int id = 102;
        String originalUsername = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        // Create a user instance that represents the existing user.
        User existingUser = new User(id, originalUsername, password, name, role);

        // Simulate the user is found for the provided ID.
        when(mockUserDao.getUser(id)).thenReturn(existingUser);

        // No other user has the new username, so getUsers() can return just the existing user for simplicity.
        when(mockUserDao.getUsers()).thenReturn(new User[] { existingUser });

        String newUsername = "newUser";

        // Simulate that the update operation fails for some reason, indicated by returning null.
        when(mockUserDao.updateUsername(existingUser, newUsername)).thenReturn(null);

        // Attempt to update the username.
        ResponseEntity<User> response = UserController.updateUsername(id, newUsername);

        // Verify that the operation failed and returned HttpStatus.CONFLICT.
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
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

        when(mockUserDao.getUsers()).thenReturn(new User[]{ u });
        when(mockUserDao.createUser(u)).thenReturn(u);
        ResponseEntity<User> r = UserController.registerUser(u);
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

        when(mockUserDao.createUser(u)).thenReturn(u);
        ResponseEntity<User> r = UserController.registerUser(u);
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

        when(mockUserDao.createUser(u)).thenReturn(u);
        ResponseEntity<User> r = UserController.registerUser(u);
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

        when(mockUserDao.createUser(u)).thenReturn(u);
        ResponseEntity<User> r = UserController.registerUser(u);
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

        when(mockUserDao.createUser(u)).thenReturn(u);
        ResponseEntity<User> r = UserController.registerUser(u);
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

        when(mockUserDao.createUser(u)).thenReturn(u);
        ResponseEntity<User> r = UserController.registerUser(u);
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

        when(mockUserDao.createUser(u)).thenReturn(u);
        ResponseEntity<User> r = UserController.registerUser(u);
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

        when(mockUserDao.createUser(u)).thenReturn(u);
        ResponseEntity<User> r = UserController.registerUser(u);
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

        when(mockUserDao.createUser(u)).thenReturn(u);
        ResponseEntity<User> r = UserController.registerUser(u);
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
        int id = 102;
        User.UserRole role = User.UserRole.CUSTOMER;
        User expectedUser = new User(id, username, password, "User Name", role);

        when(mockUserDao.getUsers()).thenReturn(new User[]{ expectedUser });
        when(mockUserDao.authorize(username, password)).thenReturn(true);

        // Execute the login method. 
        ResponseEntity<LoginResponse> response = UserController.login(username, password);

        // Validate the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getUserId());
        assertEquals("admin", response.getBody().getUserType()); 
        assertEquals("admin login successful", response.getBody().getMessage());
    }

    @Test
    public void testLoginAdminFail() {
        String username = "admin";
        String password = "000000";
        // Simulate authorization failure
        when(mockUserDao.getUsers()).thenReturn(new User[]{});
        when(mockUserDao.authorize(username, password)).thenReturn(false);

        ResponseEntity<LoginResponse> response = UserController.login(username, password);

        // Validate the response for failed login
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getUserId());
        assertEquals("none", response.getBody().getUserType());
        assertEquals("Invalid username or password", response.getBody().getMessage());
    }

    @Test
public void testLoginUser() {
    // Setup mock behavior
    int id = 102;
    String username = "testUser";
    String password = "000000";
    User.UserRole role = User.UserRole.CUSTOMER;
    User expectedUser = new User(id, username, password, "User Name", role);

    when(mockUserDao.getUsers()).thenReturn(new User[]{ expectedUser });
    when(mockUserDao.authorize(username, password)).thenReturn(true);

    // Execute the login method
    ResponseEntity<LoginResponse> response = UserController.login(username, password);

    // Assert the response details
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(id, response.getBody().getUserId());
    assertEquals("user", response.getBody().getUserType());
    assertEquals("user login successful", response.getBody().getMessage());
}


@Test
public void testLoginFail() {
    // Setup mock behavior
    String username = "user";
    String password = "wrongPassword";

    when(mockUserDao.getUsers()).thenReturn(new User[]{});
    when(mockUserDao.authorize(username, password)).thenReturn(false);

    // Execute the login method
    ResponseEntity<LoginResponse> response = UserController.login(username, password);

    // Assert the response details
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(0, response.getBody().getUserId()); // Assuming 0 indicates an unauthorized or nonexistent user
    assertEquals("none", response.getBody().getUserType());
    assertEquals("Invalid username or password", response.getBody().getMessage());
}
}

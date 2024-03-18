package com.estore.api.estoreapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the User Model Class
 *
 * @author Matthew Morrison msm8275
 */
@Tag("Model-tier")
public class UserTest {

    @BeforeEach
    public void userSetUp() {
        User admin = new User(100, "admin", "1234",
                "admin", User.UserRole.ADMIN);
        User testCustomer = new User(101, "test", "0000",
                "John Doe", User.UserRole.CUSTOMER);

    }

    @Test
    public void testNewUser(){
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);
        assertEquals(id, u.getId());
        assertEquals(username, u.getUsername());
        assertEquals(password, u.getPassword());
        assertEquals(name, u.getName());
        assertEquals(role, u.getRole());

    }

    @Test
    public void testName(){
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);


        String newName = "nameName";

        u.setName(newName);

        assertEquals(newName, u.getName());
    }

    @Test
    public void testUsername(){
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);


        String newUsername = "myUsername";

        u.setUsername(newUsername);

        assertEquals(newUsername, u.getUsername());
    }

    @Test
    public void testPassword(){
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);


        String newPassword = "IlovePasswords";

        u.setPassword(newPassword);

        assertEquals(newPassword, u.getPassword());
    }

    @Test
    public void testRole(){
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);


        User.UserRole newRole = User.UserRole.ADMIN;

        u.setRole(newRole);

        assertEquals(newRole, u.getRole());
    }

    @Test
    public void testToString(){
        // Expected values
        int id = 102;
        String username = "testUser";
        String password = "000000";
        String name = "New User";
        User.UserRole role = User.UserRole.CUSTOMER;

        User u = new User(id, username, password, name, role);


       String expected_ts = Integer.toString(id) + username +
               password + name + role.toString();

       assertEquals(expected_ts, u.toString());
    }

}

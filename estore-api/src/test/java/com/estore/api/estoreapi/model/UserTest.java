package com.estore.api.estoreapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the User Model Class
 *
 * @author Matthew Morrison msm8275
 */
@Tag("Model-tier")
public class UserTest {

    private User[] users;
    private User admin;
    private User testCustomer;

    @BeforeEach
    public void userSetUp() {
        admin = new User(100, "admin", "1234",
                "admin", User.UserRole.ADMIN);
        testCustomer = new User(101, "test", "0000",
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
}

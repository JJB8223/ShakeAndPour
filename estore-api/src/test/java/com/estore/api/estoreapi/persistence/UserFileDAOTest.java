package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test the User File DAO Class
 *
 * @author Matthew Morrison msm8275
 */
@Tag("Persistence-tier")
public class UserFileDAOTest {
    UserFileDAO userFileDAO;
    User[] testUsers;
    Map<String, String> testLoginCreds;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setupUserFileDAO() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        testUsers = new User[2];
        User admin = new User(100, "admin", "1234",
                "admin", User.UserRole.ADMIN);
        User testCustomer = new User(101, "test", "0000",
                "John Doe", User.UserRole.CUSTOMER);

        testUsers[0] = admin;
        testUsers[1] = testCustomer;

        testLoginCreds = new HashMap<>();

        testLoginCreds.put(testUsers[0].getUsername(), testUsers[0].getPassword());
        testLoginCreds.put(testUsers[1].getUsername(), testUsers[1].getPassword());

        when(mockObjectMapper
                .readValue(new File("doesnt_matter.txt"), User[].class))
                .thenReturn(testUsers);
        userFileDAO = new UserFileDAO("doesnt_matter.txt",mockObjectMapper);
    }


    @Test
    public void testGetUsers()  {
        User[] users = userFileDAO.getUsers();

        assertEquals(users.length, testUsers.length);

        for(int i = 0; i < testUsers.length; i++) {
            assertEquals(users[i], testUsers[i]);
        }
    }

    @Test
    public void testCreateUser() throws IOException {
        User u = new User(102, "user" , "pass", "User", User.UserRole.CUSTOMER);
        User r = assertDoesNotThrow(() -> userFileDAO.createUser(u),
                "Unexpected exception thrown");

        assertNotNull(r);

        User actual = userFileDAO.getUser(u.getId());

        assertEquals(actual.getId(), u.getId());
        assertEquals(actual.getUsername(), u.getUsername());
        assertEquals(actual.getPassword(), u.getPassword());
        assertEquals(actual.getName(), u.getName());
        assertEquals(actual.getRole(), u.getRole());
    }

    @Test
    public void testDeleteUser(){
        boolean r = assertDoesNotThrow(() -> userFileDAO.deleteUser(100),
                "Unexpected exception thrown");
        boolean fail = assertDoesNotThrow(() -> userFileDAO.deleteUser(88),
                "Unexpected exception thrown");
        assertFalse(fail);
        assertTrue(r);
        assertEquals(userFileDAO.users.size(), testUsers.length-1);
    }

    @Test
    public void testGetUser(){
        User u = userFileDAO.getUser(100);
        assertEquals(u, testUsers[0]);

        u = userFileDAO.getUser(101);
        assertNull(userFileDAO.getUser(200));

        assertEquals(u, testUsers[1]);
    }

    @Test
    public void testUpdateUsername() throws IOException{
        User u = new User(102, "user" , "pass", "User", User.UserRole.CUSTOMER);

        userFileDAO.createUser(u);

        String newUsername = "newuser";

        User result = assertDoesNotThrow(() -> userFileDAO.updateUsername(u, newUsername),
                "Unexpected exception thrown");

        User notFound = new User(0, "NotFound", "000", "u", User.UserRole.CUSTOMER);

        assertNull(userFileDAO.updateUsername(notFound, newUsername));

        assertNotNull(result);

        assertEquals(102, u.getId());
        assertEquals(newUsername, u.getUsername());
        assertEquals("pass", u.getPassword());
        assertEquals("User", u.getName());
        assertEquals(User.UserRole.CUSTOMER, u.getRole());


        testLoginCreds = userFileDAO.getLogin();
        assertEquals(testLoginCreds.size(), 3);
        assertTrue(testLoginCreds.containsKey(newUsername));
        assertEquals(testLoginCreds.get(newUsername), u.getPassword());


    }
}

package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Implements the functionality for JSON file-based persistence for Users
 *
 * @author Matthew Morrison msm8275
 */
public class UserFileDAO implements UserDAO{

    Map<Integer, User> users; // local cache of all current Users

    private ObjectMapper objectMapper; // Connection between User objects
                                        // and JSON text format written
                                        // to the file

    private static int nextId; // Next id assigned to a new User

    private String filename; // Filename to read from and write to

    /**
     * Instantiate the User File DAO
     * @param filename the name of the file containing User data
     * @param objectMapper the object mapper between User objects and JSON text
     * @throws IOException if an error occurs when instantiating the file
     */
    public UserFileDAO(@Value("${user.file") String filename, ObjectMapper objectMapper) throws IOException{
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    /**
     * Load the information on the user file
     * @return true when completed
     * @throws IOException if an error occurs reading the object mapper
     */
    private boolean load() throws IOException {
        users = new TreeMap<>();
        nextId = 0;

        User[] UserArray = objectMapper.readValue(new File(filename), User[].class);

        for (User user: UserArray) {
            users.put(user.getId(), user);
            if (user.getId() > nextId) {
                nextId = user.getId();
            }
        }
        nextId++;
        return true;
    }

    /**
     * Create and save a new  {@linkplain User User}
     * @param user {@linkplain User User} object to be created and saved.
     * The id of the User object is assigned uniquely when a new User is created
     *
     * @return new {@linkplain User User} if successful, False otherwise
     * @throws IOException if there is an issue with underlying storage
     */
    @Override
    public User createUser(User user) throws IOException {
        return null;
    }

    /**
     * Delete a {@linkplain User User} with their id
     *
     * @param id The id of the {@link User User}
     *
     * @return true if the {@link User User} was deleted,
     * False, if User with the given id does not exist
     *
     * @throws IOException if underlying storage cannot be accessed
     */
    @Override
    public boolean deleteProduct(int id) throws IOException {
        return false;
    }

    /**
     * Updates and saves a new username for a {@linkplain User User}
     *
     * @param user {@linkplain User User} user to update their username
     * @param newUsername the new username to update to
     *
     * @return updated {@linkplain User User} if successful, null if
     * {@linkplain User User} could not be found
     *
     * @throws IOException if underlying storage cannot be accessed
     */
    @Override
    public User updateUsername(User user, String newUsername) throws IOException {
        return null;
    }

    /**
     * Updates and saves a new password for a {@linkplain User User}
     *
     * @param user {@linkplain User User} user to update their username
     * @param newPassword the new password to update to
     *
     * @return updated {@linkplain User User} if successful, null if
     * {@linkplain User User} could not be found
     *
     * @throws IOException if underlying storage cannot be accessed
     */
    @Override
    public User updatePassword(User user, String newPassword) throws IOException {
        return null;
    }

    /**
     * Updates and saves a new name for a {@linkplain User User}
     *
     * @param user {@linkplain User User} user to update their username
     * @param newName the new name to update to
     *
     * @return updated {@linkplain User User} if successful, null if
     * {@linkplain User User} could not be found
     *
     * @throws IOException if underlying storage cannot be accessed
     */
    @Override
    public User updateName(User user, String newName) throws IOException {
        return null;
    }
}

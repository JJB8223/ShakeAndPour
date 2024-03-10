package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.User;

import java.io.IOException;

/**
 * Defines the interface for the User object persistence
 *
 * @author Matthew Morrison msm8275
 */
public interface UserDAO {

    /**
     * Create and save a new  {@linkplain User User}
     * @param user {@linkplain User User} object to be created and saved.
     * The id of the User object is assigned uniquely when a new User is created
     *
     * @return new {@linkplain User User} if successful, False otherwise
     * @throws IOException if there is an issue with underlying storage
     */
    User createUser (User user) throws IOException;

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
    boolean deleteUser(int id) throws IOException;

    /**
     * Generates an array of {@linkplain User users} from the tree map for any
     * {@linkplain User users}
     *
     * @return  The array of {@link Product products}, may be empty
     */
    User[] getUsers();

    /**
     * Retrieve a specific {@linkplain User User}
     *
     * @param id the id of the user to find
     *
     * @return the specific {@linkplain User User} that corresponds
     * with the id
     *
     * @throws IOException if there is an issue with underlying storage
     */
    User getUser(int id) throws IOException;

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
    User updateUsername(User user, String newUsername) throws IOException;

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
    User updatePassword(User user, String newPassword) throws IOException;

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
    User updateName(User user, String newName) throws IOException;

    /**
     * Attempt to log in a user to the estore with their credentials
     * @param username the entered username
     * @param password the entered password
     * @return true if the credentials exists and are correct, else false
     */
    boolean authorize(String username, String password);
}

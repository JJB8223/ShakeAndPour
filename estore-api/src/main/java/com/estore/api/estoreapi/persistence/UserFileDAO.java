package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
     * Generates the next id for a new {@linkplain User User}
     *
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
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
     * Generates an array of {@linkplain User users} from the tree map for any
     * {@linkplain User users}
     *
     * @return  The array of {@link Product products}, may be empty
     */
    private User[] getUsers() {
        ArrayList<User> userArrayList = new ArrayList<>(users.values());

        User[] userList = new User[userArrayList.size()];
        userArrayList.toArray(userList);
        return userList;
    }

    /**
     * Saves the {@linkplain Product products} from the map into the file as an array of JSON objects
     *
     * @return true if the {@link Product products} were written successfully
     *
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        User[] users = getUsers();

        objectMapper.writeValue(new File(filename), users);
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
        synchronized(users) {
            User newU = new User(nextId(), user.getUsername(), user.getPassword(),
                    user.getName(), user.getRole());
            users.put(newU.getId(), newU);
            save();
            return newU;
        }
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
    public boolean deleteUser(int id) throws IOException {
        synchronized (users){
            if(users.containsKey(id)){
                users.remove(id);
                return save();
            }
            else{
                return false;
            }
        }
    }

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
    public User getUser(int id) throws IOException{
        synchronized (users){
            return users.getOrDefault(id, null);
        }
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
        synchronized (users){
            if(!users.containsKey(user.getId())){
                return null; // User doesn't exist
            }

            user.setUsername(newUsername);
            users.put(user.getId(), user);
            save();
            return user;
        }
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
        synchronized (users){
            if(!users.containsKey(user.getId())){
                return null; // User doesn't exist
            }

            user.setPassword(newPassword);
            users.put(user.getId(), user);
            save();
            return user;
        }
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
        synchronized (users){
            if(!users.containsKey(user.getId())){
                return null; // User doesn't exist
            }

            user.setName(newName);
            users.put(user.getId(), user);
            save();
            return user;
        }
    }
}

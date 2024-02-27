package com.estore.api.estoreapi.persistence;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import com.estore.api.estoreapi.model.kit;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

@Component
public class KitFileDAO implements KitDAO{
     private static final Logger LOG = Logger.getLogger(KitFileDAO.class.getName());

    Map<Integer, kit> inventory; // local cache of all kit objects in the
                                    // current inventory

    private ObjectMapper objectMapper; // Provides conversion between kit
                                        // objects and JSON text format written
                                        // to the file

    private static int nextId;  // The next Id to assign to a new kit

    private String filename;    // Filename to read from and write to
    
    public KitFileDAO(@Value("${kits.file}") String filename, ObjectMapper objectMapper) throws IOException{
        this.filename = filename; // For whatever reason this is not working properly on my windows machine, trying to manually set the file path first then do it properly through REST 
        this.objectMapper = objectMapper;
        // TODO: load function
        load();
    }

    private boolean load() throws IOException {
        inventory = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of kits
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        kit[] kitArrary = objectMapper.readValue(new File(filename),kit[].class);

        // Add each kit to the tree map and keep track of the greatest id
        for (kit kit : kitArrary) {
            inventory.put(kit.getId(),kit);
            if (kit.getId() > nextId)
                nextId = kit.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    private kit[] getInventory(){return getInventory(null);}

        /**
     * Generates an array of {@linkplain kit kits} from the tree map for any
     * {@linkplain kit kits} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain kit kits}
     * in the tree map
     *
     * @return  The array of {@link kit kits}, may be empty
     */
    private kit[] getInventory(String containsText) {
        ArrayList<kit> inventoryArrayList = new ArrayList<>();

        for (kit k : inventory.values()){
            if (containsText == null || k.getName().contains(containsText)){
                inventoryArrayList.add(k);
            }
        }

        kit[] inventoryList  = new kit[inventoryArrayList.size()];
        inventoryArrayList.toArray(inventoryList);
        return inventoryList;
    }

    private kit[] getKitsArray(String containsText) { // if containsText == null, no filter
        ArrayList<kit> KitArrayList = new ArrayList<>();

        for (kit kit : inventory.values()) {
            if (containsText == null || kit.getName().contains(containsText)) {
                KitArrayList.add(kit);
            }
        }

        kit[] kitArray = new kit[KitArrayList.size()];
        KitArrayList.toArray(kitArray);
        return kitArray;
    }



    @Override
    public kit[] findKits(String containsText) {
        synchronized(inventory) {
            return getKitsArray(containsText);
        }
    }


    /**
     * Saves the {@linkplain kit kits} from the map into the file as an array of JSON objects
     *
     * @return true if the {@link kit kits} were written successfully
     *
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        kit[] inventory = getInventory();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),inventory);
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public kit getKit(int id) {
        synchronized(inventory) {
            if (inventory.containsKey(id))
                return inventory.get(id);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public kit[] getKits() {
        synchronized(inventory) {
            return getInventory();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteKit(int id) throws IOException {
        synchronized(inventory) {
            if (inventory.containsKey(id)) {
                inventory.remove(id);
                return save();
            }
            else
                return false;
        }
    }

    @Override
    public kit createKit(kit kit) throws IOException {
        synchronized(inventory) {
            // create new kit object, assign the next unique id to it
            kit newP = new kit(nextId(), kit.getName(), kit.getPrice(),
                                kit.getQuantity(), kit.getProductsInKit());
            inventory.put(newP.getId(), newP);
            save();
            return newP;
        }
    }

    /**
     * Updates and saves a {@linkplain kit kit}
     * 
     * @param {@link kit kit} object to be updated and saved
     * 
     * @return updated {@link kit kit} if successful, null if
     * {@link kit kit} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    public kit updateKit(kit kit) throws IOException {
        synchronized(inventory) {
            if (inventory.containsKey(kit.getId()) == false)
                return null;  // kit does not exist so we can't update it

            inventory.put(kit.getId(),kit);
            save(); // may throw an IOException
            return kit;
        }
    }


}

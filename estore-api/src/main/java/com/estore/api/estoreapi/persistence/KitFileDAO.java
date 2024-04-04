package com.estore.api.estoreapi.persistence;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import com.estore.api.estoreapi.model.Kit;
import com.estore.api.estoreapi.model.Product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

@Component
public class KitFileDAO implements KitDAO{
     private static final Logger LOG = Logger.getLogger(KitFileDAO.class.getName());

    Map<Integer, Kit> inventory; // local cache of all kit objects in the
                                    // current inventory

    private ObjectMapper objectMapper; // Provides conversion between kit
                                        // objects and JSON text format written
                                        // to the file

    private static int nextId;  // The next Id to assign to a new kit

    private String filename;    // Filename to read from and write to
    
    public KitFileDAO(@Value("${kits.file}") String filename, ObjectMapper objectMapper) throws IOException{
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    private boolean load() throws IOException {
        inventory = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of kits
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Kit[] kitArrary = objectMapper.readValue(new File(filename), Kit[].class);

        // Add each kit to the tree map and keep track of the greatest id
        for (Kit kit : kitArrary) {
            inventory.put(kit.getId(),kit);
            if (kit.getId() > nextId)
                nextId = kit.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
     * Generates the next id for a new {@linkplain Kit Kit}
     *
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Kit kits} from the tree map
     *
     * @return  The array of {@link Kit kits}, may be empty
     */
    private Kit[] getInventory(){return getInventory(null);}

    /**
     * Generates an array of {@linkplain Kit kits} from the tree map for any
     * {@linkplain Kit kits} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Kit kits}
     * in the tree map
     *
     * @return  The array of {@link Kit kits}, may be empty
     */
    private Kit[] getInventory(String containsText) {
        ArrayList<Kit> inventoryArrayList = new ArrayList<>();

        for (Kit k : inventory.values()){
            if ((containsText == null || k.getName().contains(containsText)) && k.getQuantity() != 0){
                inventoryArrayList.add(k);
            }
        }

        Kit[] inventoryList  = new Kit[inventoryArrayList.size()];
        inventoryArrayList.toArray(inventoryList);
        return inventoryList;
    }

    /**
        * Generates an array of {@linkplain Kit kits} from the tree map for any
        * {@linkplain Kit kits} that contains the text specified by containsText
        * <br>
        * If containsText is null, the array contains all of the {@linkplain Kit kits}
        * in the tree map
        * 
        * @return  The array of {@link Kit kits}, may be empty
     */
    private Kit[] getKitsArray(String containsText) { // if containsText == null, no filter
        ArrayList<Kit> KitArrayList = new ArrayList<>();

        for (Kit kit : inventory.values()) {
            String kitName = kit.getName().toLowerCase(); // most users are not going to enter the correct casing when searching for kits
            if (containsText == null || kitName.contains(containsText.toLowerCase())) {
                KitArrayList.add(kit);
            }
        }

        Kit[] kitArray = new Kit[KitArrayList.size()];
        KitArrayList.toArray(kitArray);
        return kitArray;
    }



    @Override
    public Kit[] findKits(String containsText) {
        synchronized(inventory) {
            return getKitsArray(containsText);
        }
    }


    /**
     * Saves the {@linkplain Kit kits} from the map into the file as an array of JSON objects
     *
     * @return true if the {@link Kit kits} were written successfully
     *
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Kit[] inventory = getInventory();

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
    public Kit getKit(int id) {
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
    public Kit[] getKits() {
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
    public Kit createKit(Kit kit) throws IOException {
        synchronized(inventory) {
            // create new kit object, assign the next unique id to it
            Kit newP = new Kit(nextId(), kit.getName(), kit.getPrice(),
                                kit.getQuantity(), kit.getProductsInKit());
            inventory.put(newP.getId(), newP);
            save();
            return newP;
        }
    }

    /**
     * Updates and saves a {@linkplain Kit kit}
     * 
     * @param {@link kit kit} object to be updated and saved
     * 
     * @return updated {@link Kit kit} if successful, null if
     * {@link Kit kit} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    public Kit updateKit(Kit kit) throws IOException {
        synchronized(inventory) {
            if (inventory.containsKey(kit.getId()) == false)
                return null;  // kit does not exist so we can't update it

            inventory.put(kit.getId(),kit);
            save(); // may throw an IOException
            return kit;
        }
    }


}

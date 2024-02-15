package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Implements the functionality for JSON file-based persistence for Inventory
 *
 * @author Matthew Morrison
 */
public class InventoryFileDAO  implements InventoryDAO{
    private static final Logger LOG = Logger.getLogger(InventoryFileDAO.class.getName());

    Map<Integer, Product> inventory; // local cache of all Product objects in the
                                    // current inventory

    private ObjectMapper objectMapper; // Provides conversion between Product
                                        // objects and JSON text format written
                                        // to the file

    private static int nextId;  // The next Id to assign to a new Product

    private String filename;    // Filename to read from and write to


    public InventoryFileDAO(@Value("${inventory.file") String filename,
                            ObjectMapper objectMapper){
        this.filename = filename;
        this.objectMapper = objectMapper;
        // TODO load function

    }

    /**
     * Generates the next id for a new {@linkplain Product Product}
     *
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Product products} from the tree map
     *
     * @return  The array of {@link Product products}, may be empty
     */
    private Product[] getInventory(){return getInventory(null);}

    /**
     * Generates an array of {@linkplain Product products} from the tree map for any
     * {@linkplain Product products} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Product products}
     * in the tree map
     *
     * @return  The array of {@link Product products}, may be empty
     */
    private Product[] getInventory(String containsText) {
        ArrayList<Product> inventoryArrayList = new ArrayList<>();

        for (Product p : inventory.values()){
            if (containsText == null || p.getName().contains(containsText)){
                inventoryArrayList.add(p);
            }
        }

        Product[] inventoryList  = new Product[inventoryArrayList.size()];
        inventoryArrayList.toArray(inventoryList);
        return inventoryList;

    }

    /**
     * Saves the {@linkplain Product products} from the map into the file as an array of JSON objects
     *
     * @return true if the {@link Product products} were written successfully
     *
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Product[] inventory = getInventory();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),inventory);
        return true;
    }

    @Override
    public Product createProduct(Product Product) throws IOException {
        // create new product object, assign the next unique id to it
        Product newP = new Product(nextId(), Product.getName());
        inventory.put(newP.getId(), newP);
        save();
        return newP;
    }
}

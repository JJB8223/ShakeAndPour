package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * Implements the functionality for JSON file-based persistence for Inventory
 *
 * @author Matthew Morrison
 * @author Akhil Devarapalli ad7171
 */
@Component
public class InventoryFileDAO  implements InventoryDAO{
    private static final Logger LOG = Logger.getLogger(InventoryFileDAO.class.getName());

    Map<Integer, Product> inventory; // local cache of all Product objects in the
                                    // current inventory

    private ObjectMapper objectMapper; // Provides conversion between Product
                                        // objects and JSON text format written
                                        // to the file

    private static int nextId;  // The next Id to assign to a new Product

    private String filename;    // Filename to read from and write to


    public InventoryFileDAO(@Value("${inventory.file}") String filename, ObjectMapper objectMapper) throws IOException{
        this.filename = filename; // For whatever reason this is not working properly on my windows machine, trying to manually set the file path first then do it properly through REST 
        this.objectMapper = objectMapper;
        load();

    }

    private boolean load() throws IOException {
        inventory = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of products
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Product[] ProductArray = objectMapper.readValue(new File(filename),Product[].class);

        // Add each Product to the tree map and keep track of the greatest id
        for (Product product : ProductArray) {
            inventory.put(product.getId(),product);
            if (product.getId() > nextId)
                nextId = product.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
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
        * Generates an array of {@linkplain Product products} from the tree map for any
        * {@linkplain Product products} that contains the text specified by containsText
        * <br>
        * If containsText is null, the array contains all of the {@linkplain Product products}
        * in the tree map
        * 
        * @return  The array of {@link Product products}, may be empty
     */
    private Product[] getProductsArray(String containsText) { // if containsText == null, no filter
        ArrayList<Product> ProductArrayList = new ArrayList<>();

        for (Product product : inventory.values()) {
            if (containsText == null || product.getName().contains(containsText)) {
                ProductArrayList.add(product);
            }
        }

        Product[] ProductArray = new Product[ProductArrayList.size()];
        ProductArrayList.toArray(ProductArray);
        return ProductArray;
    }


    @Override
    public Product[] findProducts(String containsText) {
        synchronized(inventory) {
            return getProductsArray(containsText);
        }
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

    /**
    ** {@inheritDoc}
     */
    @Override
    public Product getProduct(int id) {
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
    public Product[] getProducts() {
        synchronized(inventory) {
            return getInventory();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteProduct(int id) throws IOException {
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
    public Product createProduct(Product Product) throws IOException {
        synchronized(inventory) {
            // create new product object, assign the next unique id to it
            Product newP = new Product(nextId(), Product.getName(), Product.getPrice(),
                    Product.getQuantity());
            inventory.put(newP.getId(), newP);
            save();
            return newP;
        }
    }

    /**
     * Updates and saves a {@linkplain Product Product}
     * 
     * @param {@link Product Product} object to be updated and saved
     * 
     * @return updated {@link Product Product} if successful, null if
     * {@link Product Product} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    public Product updateProduct(Product product) throws IOException {
        synchronized(inventory) {
            if (inventory.containsKey(product.getId()) == false)
                return null;  // Product does not exist so we can't update it

            inventory.put(product.getId(),product);
            save(); // may throw an IOException
            return product;
        }
    }
}

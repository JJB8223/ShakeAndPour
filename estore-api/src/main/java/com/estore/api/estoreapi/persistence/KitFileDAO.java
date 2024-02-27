package com.estore.api.estoreapi.persistence;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import com.estore.api.estoreapi.model.Product;
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

    private ObjectMapper objectMapper; // Provides conversion between Product
                                        // objects and JSON text format written
                                        // to the file

    private static int nextId;  // The next Id to assign to a new Product

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

        // Deserializes the JSON objects from the file into an array of products
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        kit[] kitArrary = objectMapper.readValue(new File(filename),kit[].class);

        // Add each Product to the tree map and keep track of the greatest id
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




}

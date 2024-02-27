package com.estore.api.estoreapi.controller;

import com.estore.api.estoreapi.model.Kit;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.persistence.KitDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the REST API requests for custom kits in the e-store
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 *
 * @author Matthew Morrison msm8275
 */
@RestController
@RequestMapping("kits")
public class KitController {
    private static final Logger LOG = Logger.getLogger(ProductController.class.getName());
    private KitDAO kitDao;

    /**
     * Creates a REST API controller to respond to requests
     * @param KitDao The {@link KitDAO Kit Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public KitController(KitDAO KitDao){
        this.kitDao = KitDao;
    }

    /**
     * Creates a {@linkplain Kit Kit} with the provided Product object
     *
     * @param Kit - The {@link Kit Kit} to create
     *
     * @return ResponseEntity with created {@link Kit Kit} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Kit Kit} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * @throws IOException if error occurs with the server
     */
    @PostMapping("/create")
    public ResponseEntity<Kit> createKit(@RequestBody Kit Kit) throws IOException {
        LOG.info("POST /kits/create" + Kit);
        try{
            Kit createdKit = kitDao.createKit(Kit);
            if (createdKit == null){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(createdKit, HttpStatus.CREATED);
        } catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Kit Kit} whose name contains
     * the text in name
     *
     * @param name The name parameter which contains the text used to find the {@linkplain Kit Kit}
     *
     * @return ResponseEntity with array of {@linkplain Kit Kit} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all products that contain the text "ma"
     * GET http://localhost:8080/kit/?name=ma
     * @throws IOException if error occurs trying to get any kits
     */
    @GetMapping("/")
    public ResponseEntity<Kit[]> searchKits(@RequestParam String name) throws IOException{
        LOG.info("GET /kits/?name="+name);
        try{
            Kit[] kits = kitDao.findKits(name);
            return new ResponseEntity<>(kits, HttpStatus.OK);
        } catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Kit Kit}
     *
     * @return ResponseEntity with array of {@linkplain Kit Kit} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * @throws IOException if an error occurs obtaining the kits
     */
    @GetMapping("")
    public ResponseEntity<Kit[]> getKits() throws IOException {
        LOG.info("GET /kits");
        try {
            Kit[] kits = kitDao.getKits();
            return new ResponseEntity<>(kits, HttpStatus.OK);
        } catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for a {@linkplain Kit Kit} for the given id
     *
     * @param id The id used to locate the {@linkplain Kit Kit}
     *
     * @return ResponseEntity with created {@linkplain Kit Kit} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Kit> getKit(@PathVariable int id) {
        LOG.info("GET /kit/" + id);
        try{
            Kit kit = kitDao.getKit(id);
            if (kit != null){
                return new ResponseEntity<>(kit, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Kit Kit} with the provided {@linkplain Kit Kit} object, if it exists
     *
     * @param Kit The {@linkplain Kit Kit} to update
     *
     * @return ResponseEntity with updated {@link Product Product} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Kit> updateKit(@RequestBody Kit Kit){
        LOG.info("PUT /kits " + Kit);
        try {
            Kit status = kitDao.updateKit(Kit);
            if (status == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(status, HttpStatus.OK);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Deletes a {@linkplain Kit Kit} with the given id
     *
     * @param id The id of the {@linkplain Kit Kit} to deleted
     *
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Kit> deleteKit(@PathVariable int id) {
        LOG.info("DELETE /kit/" + id);
        try{
            boolean status = kitDao.deleteKit(id);
            if (!status) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

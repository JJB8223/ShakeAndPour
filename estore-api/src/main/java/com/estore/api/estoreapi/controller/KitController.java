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
@RequestMapping("kit")
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
        LOG.info("POST /kit/create" + Kit);
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

    @GetMapping("/")
    public ResponseEntity<Kit[]> searchKits(@RequestParam String name) throws IOException{
        LOG.info("GET /kit/?name="+name);
        try{
            Kit[] kits = kitDao.findKits(name);
            return new ResponseEntity<>(kits, HttpStatus.OK);
        } catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

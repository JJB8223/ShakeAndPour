package com.estore.api.estoreapi.controller;

import com.estore.api.estoreapi.persistence.KitDAO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private KitDAO KitDao;

    /**
     * Creates a REST API controller to respond to requests
     * @param KitDao The {@link KitDAO Kit Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public KitController(KitDAO KitDao){
        this.KitDao = KitDao;
    }


}

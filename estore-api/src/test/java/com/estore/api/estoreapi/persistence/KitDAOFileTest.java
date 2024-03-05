package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.Kit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;



@Tag("Persistence-tier")
public class KitDAOFileTest {
    KitFileDAO kitFileDAO;
    Kit[] testKits;
    ObjectMapper mockObjectMapper;
}

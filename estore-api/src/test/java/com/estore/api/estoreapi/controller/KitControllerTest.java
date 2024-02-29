package com.estore.api.estoreapi.controller;

import com.estore.api.estoreapi.model.Kit;
import com.estore.api.estoreapi.persistence.KitDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("Controller-tier")
public class KitControllerTest {

    private KitDAO mockKitDAO;
    private KitController KitController;

    @BeforeEach
    public void setUpKitController(){
        mockKitDAO = mock(KitDAO.class);
        KitController = new KitController((mockKitDAO));

    }

    @Test
    public void testCreateKit() throws IOException{
        ArrayList<Integer> productIds = new ArrayList<>();
        productIds.add(1);
        productIds.add(2);
        productIds.add(3);
        // Setup
        Kit p = new Kit(99, "Soda", 2.99f, 20, productIds);
        // when createInventory is called, return true simulating successful
        // creation and save
        when(mockKitDAO.createKit(p)).thenReturn(p);

        // invoke
        ResponseEntity<Kit> response = KitController.createKit(p);

        // analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(p,response.getBody());
    }

    @Test
    public void testKitSearch() throws IOException {
        String searchString = "la";
        ArrayList<Integer> productIdOne = new ArrayList<>();
        productIdOne.add(1);
        productIdOne.add(2);
        productIdOne.add(3);

        ArrayList<Integer> productIdTwo = new ArrayList<>();
        productIdTwo.add(5);
        productIdTwo.add(6);
        productIdTwo.add(7);

        Kit[] kits = new Kit[2];
        kits[0] = new Kit(99, "Lala paloza kit", 35.99f, 30, productIdTwo);
        kits[1] = new Kit(98, "RaLa la paloza kit", 35.99f, 30, productIdOne);

        when(mockKitDAO.findKits(searchString)).thenReturn(kits); // says to mockito we want to return this thing

        ResponseEntity<Kit[]> response = KitController.searchKits(searchString);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(kits, response.getBody());
    
    }

    @Test
    public void testKitSearchFail() throws IOException {
        // Setup
        String searchString = "an";
        // When createProduct is called on the Mock product DAO, throw an IOException
        doThrow(new IOException()).when(mockKitDAO).findKits(searchString);
 
        // Invoke
        ResponseEntity<Kit[]> response = KitController.searchKits(searchString);
 
        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateKitFail() throws IOException{
        // Setup
        ArrayList<Integer> productIds = new ArrayList<>();
        productIds.add(1);
        productIds.add(2);
        productIds.add(3);
        // Setup
        Kit p = new Kit(99, "Soda", 2.99f, 20, productIds);
        // when createInventory is called, return false simulating failed
        // creation and save
        when(mockKitDAO.createKit(p)).thenReturn(null);

        // invoke
        ResponseEntity<Kit> response = KitController.createKit(p);

        // analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testGetKits() throws IOException {
        ArrayList<Integer> productIdOne = new ArrayList<>();
        productIdOne.add(1);
        productIdOne.add(2);
        productIdOne.add(3);

        ArrayList<Integer> productIdTwo = new ArrayList<>();
        productIdTwo.add(5);
        productIdTwo.add(6);
        productIdTwo.add(7);

        Kit[] kits = new Kit[2];
        kits[0] = new Kit(99, "Lala paloza kit", 35.99f, 30, productIdTwo);
        kits[1] = new Kit(98, "RaLa la paloza kit", 35.99f, 30, productIdOne);

        when(mockKitDAO.getKits()).thenReturn(kits);

        ResponseEntity<Kit[]> response = KitController.getKits();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(kits,response.getBody());
    }

    @Test
    public void testGetKitsHandleException() throws IOException {
        doThrow(new IOException()).when(mockKitDAO).getKits();

        ResponseEntity<Kit[]> response = KitController.getKits();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetKit() throws IOException {

        ArrayList<Integer> productIds = new ArrayList<>();
        productIds.add(1);
        productIds.add(2);
        productIds.add(3);
        // Setup
        Kit k = new Kit(99, "la la drink", 29.99f, 20, productIds);

        when(mockKitDAO.getKit(k.getId())).thenReturn(k);

        ResponseEntity<Kit> response = KitController.getKit(k.getId());

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(k,response.getBody());

    
    }

    @Test
    public void testGetKitNotFound() throws IOException {
        int kitID = 42;

        when(mockKitDAO.getKit(kitID)).thenReturn(null);

        ResponseEntity<Kit> response = KitController.getKit(kitID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetKitHandleException() throws IOException {
        int kitID = 42;

        doThrow(new IOException()).when(mockKitDAO).getKit(kitID);

        ResponseEntity<Kit> response = KitController.getKit(kitID);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

    @Test
    public void testDeleteKit() throws IOException {
        int kitID = 99;
        
        when(mockKitDAO.deleteKit(kitID)).thenReturn(true);

        ResponseEntity<Kit> response = KitController.deleteKit(kitID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteKitNotFound() throws IOException {
        int kitID = 99;

        when(mockKitDAO.deleteKit(kitID)).thenReturn(false);

        ResponseEntity<Kit> response = KitController.deleteKit(kitID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteKitHandleException() throws IOException {
        int productID = 99;
        doThrow(new IOException()).when(mockKitDAO).deleteKit(productID);

        ResponseEntity<Kit> response = KitController.deleteKit(productID);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }


}

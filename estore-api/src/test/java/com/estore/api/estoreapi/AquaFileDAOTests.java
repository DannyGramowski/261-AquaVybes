
package com.estore.api.estoreapi;

import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.model.aqua.PackSize;
import com.estore.api.estoreapi.persistence.aqua.AquaFileDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Arrays;

public class AquaFileDAOTests {
    AquaFileDAO dao;
    Aqua[] testInventory;
    ObjectMapper mockObjectWrapper;

    @BeforeEach
    public void init() {
        mockObjectWrapper = mock(ObjectMapper.class);
        testInventory = new Aqua[1];
        ArrayList<Flavor> flavors = new ArrayList<Flavor>(1);
        flavors.add(new Flavor(0, "lemon", "lemon desc", 5));
        testInventory[0] = new Aqua(2, "product2", flavors, PackSize.SIX, 0);
        try {
            when(mockObjectWrapper.readValue(new File("test.json"), Aqua[].class))
            .thenReturn(testInventory);
            dao = new AquaFileDAO("test.json", mockObjectWrapper);
        } catch(IOException e) {
            System.out.println(e);
        }
    }

    @Test
    public void testGetInventory() throws IOException {
        Aqua[] inventory = dao.getInventory();

        assertEquals(inventory.length, testInventory.length);
        for(int i = 0; i < testInventory.length; ++i) {
            assertEquals(inventory[i], testInventory[i]);
        }
    }

    @Test
    public void testGetAqua() throws IOException {
        ArrayList<Flavor> flavors = new ArrayList<Flavor>();
        flavors.add(0, new Flavor(1, "cherry", "cherry desc", 5));
        Aqua test = null;
        try {
        test = dao.createAqua("name", flavors,
          PackSize.SIX, 0);
        } catch (IOException e) {
            System.out.println(e);
        }
        assertTrue(dao.getAqua(test.getId()).equals(test) && dao.getAqua("name").equals(test));
    }


 
    @Test
    public void testDeleteAqua() {
        ArrayList<Flavor> flavors = new ArrayList<Flavor>();
        flavors.add(0, new Flavor(2, "cherry", "cherry desc", 5));
        try {
            Aqua test = dao.createAqua("name", flavors,
          PackSize.SIX, 0);
        assertTrue(dao.deleteAqua(test.getId()) == true && 
        dao.deleteAqua(-1) == false);
        } catch (Exception e) {
            assertTrue(false);
        }
        
    }


    /**
     * Tests the updateAqua method of the AquaDAO.
     */
    
    @Test
    public void testUpdateInventory() {
        try{
            // Hardcode new item with placeholders to test update
            Aqua curr = dao.createAqua(
                    "Test Aqua Ver 1",
                    new ArrayList<Flavor>() {{ add(new Flavor(1, "cherry", "cherry desc", 5)); }}, 
                    PackSize.SINGLE, 0);

            // Get id of created item
            final int id = curr.getId();

            // Try to update the aqua with given ID to preset values
            final ArrayList<Flavor> flavors = new ArrayList<>() {{ add(new Flavor(2, "lemon", "lemon desc", 5)); }};
            final Aqua test = dao.updateAqua(
                    curr.getId(), 
                    "Ver 2",  
                    flavors, 
                    PackSize.SIX);

            // Assert that all fields were succesfully updated for the same id
            assertAll(
                "Tests that each field successfully updates",
                () -> Assertions.assertEquals(id, test.getId()),
                () -> Assertions.assertEquals("Ver 2", test.getName()),
                () -> Assertions.assertEquals(flavors, test.getFlavors()),
                () -> Assertions.assertEquals(PackSize.SIX, test.getPackSize())
            );
        } catch(IOException e) {
            System.out.println(e);
        }
    }
}

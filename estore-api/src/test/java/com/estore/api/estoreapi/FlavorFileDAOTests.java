package com.estore.api.estoreapi;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.persistence.flavor.FlavorFileDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

public class FlavorFileDAOTests {
    static FlavorFileDAO dao;
    static ObjectMapper mockObjectMapper;
    static Flavor[] testFlavors;

    @BeforeAll
    public static void init() {
        mockObjectMapper = mock(ObjectMapper.class);
        testFlavors = new Flavor[3];
        testFlavors[0] = new Flavor(0, "cherry", "test desc", 5);
        testFlavors[1] = new Flavor(1, "blueberry", "blueberry desc", 5);
        testFlavors[2] = new Flavor(2, "lemon", "lemon desc", 5);
        try {
            when(mockObjectMapper.readValue(new File("flavorsTest.json"), Flavor[].class))
                .thenReturn(testFlavors);
            dao = new FlavorFileDAO("flavorsTest.json", mockObjectMapper);
        } catch(IOException e) {
            System.out.println(e);
        }
    }

    @Test
    public void testCreateFlavor() throws IOException {
        Flavor newFlavor = dao.createFlavor("new flavor", "new desc", 5);
        assertEquals(newFlavor, dao.getFlavor(newFlavor.getId()));
    }

    @Test
    public void testGetAllFlavors() {
        assertNotEquals(null, dao.getAllFlavors());
    }
    
    @Test
    public void testDeleteFlavor() throws IOException {
        dao.deleteFlavor(1);
        assertEquals(dao.getFlavor(1), null);
        assertEquals(false, dao.deleteFlavor(1));
    }

    @Test
    public void testGetFlavor() throws IOException {
        assertEquals(null, dao.getFlavor(1));
    }


}

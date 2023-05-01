package com.estore.api.estoreapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.flavor.Flavor;

public class FlavorTests {

    static Flavor cherry;
    
    @BeforeAll
    public static void init() {
        cherry = new Flavor(1, "cherry", "cherry description", 5);
    }
    
    @Test
    public void testCreateFlavor() {
        assertNotEquals(null, cherry);
    }

    @Test
    public void testGetId() {
        assertEquals(1, cherry.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("cherry", cherry.getName());
    }

    @Test
    public void testGetDesc() {
        assertEquals("cherry description", cherry.getDescription());
    }

    @Test
    public void testSetId() {
        cherry.setId(2);
        assertEquals(2, cherry.getId());
    }

    @Test
    public void testSetName() {
        cherry.setName("lime");
        assertEquals("lime", cherry.getName());
    }

    @Test
    public void testSetDesc() {
        cherry.setDescription("lime description");
        assertEquals("lime description", cherry.getDescription());
    }

    @Test
    public void testSetQuantity() {
        cherry.setQuantity(1);
        assertEquals(1, cherry.getQuantity());
    }

    @Test
    public void testEquals() {
        Flavor lime = new Flavor(1, "lemon", "lemon description", 1);
        Flavor lemon = new Flavor(1, "lemon", "lemon description", 1);
        assertEquals(true, cherry.equals(cherry));
        assertEquals(false, cherry.equals(null));
        assertEquals(false, cherry.equals(1));
        assertEquals(true, lime.equals(lemon));
    }
}

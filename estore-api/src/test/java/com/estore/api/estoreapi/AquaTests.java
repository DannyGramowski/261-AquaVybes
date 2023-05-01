package com.estore.api.estoreapi;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import com.estore.api.estoreapi.model.flavor.Flavor;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.aqua.PackSize;

public class AquaTests {
    static Flavor globalFlavor = new Flavor(1, "blueberry", "blueberry desc", 5);
    @Test
    public void equals() {
        ArrayList<Flavor> flavors = new ArrayList<>() {{ add(globalFlavor); }};
        Aqua aqua1 = new Aqua(0, "name", flavors, PackSize.SIX, 0);
        Aqua aqua2 = new Aqua(0, "name",  flavors, PackSize.SIX, 0);
        Aqua aqua3 = new Aqua(1, "name",  flavors, PackSize.SIX, 0);
        Aqua aqua4 = new Aqua(0, "name_1",  flavors, PackSize.SIX, 0);
        Aqua aqua6 = new Aqua(0, "name",  new ArrayList<Flavor>(), PackSize.SIX, 0);
        Aqua aqua7 = new Aqua(0, "name",  flavors, PackSize.SINGLE, 0);

        assertAll(
            () -> { assertFalse(aqua1.equals(null)); },
            () -> { assertFalse(aqua1.equals(5)); },
            () -> { assertFalse(aqua1.equals(aqua3)); },
            () -> { assertFalse(aqua1.equals(aqua4)); },
            () -> { assertFalse(aqua1.equals(aqua6)); },
            () -> { assertFalse(aqua1.equals(aqua7)); },
            () -> { assertTrue(aqua1.equals(aqua1)); },
            () -> { assertTrue(aqua1.equals(aqua2)); },
            () -> { assertTrue(aqua2.equals(aqua1)); }
        );
    }

    @Test
    public void test_to_string() {
        ArrayList<Flavor> flavors = new ArrayList<>() {{ add(globalFlavor); }};
        Aqua aqua = new Aqua(0, "name", flavors, PackSize.SINGLE, 0);
        System.out.println(aqua);
        assertEquals(
            "Aqua{id=0, name='name', flavors=[Flavor{id=1, name='blueberry', desc='blueberry desc', quantity=5}], packSize=SINGLE, orderID=0}",
            aqua.toString()
        );
    }

    @Test
    public void test_hash() {
        ArrayList<Flavor> flavors = new ArrayList<>() {{ add(globalFlavor); }};
        Aqua aqua1 = new Aqua(0, "name",  flavors, PackSize.SINGLE, 0);
        Aqua aqua2 = new Aqua(0, "name",  flavors, PackSize.SINGLE, 0);
        Aqua aqua3 = new Aqua(1, "name",  flavors, PackSize.SINGLE, 0);

        assertAll(
            () -> { assertTrue(aqua1.hashCode() == aqua1.hashCode());},
            () -> { assertTrue(aqua2.hashCode() == aqua2.hashCode());},
            () -> { assertFalse(aqua1.hashCode() == aqua3.hashCode());}
        );
    }

    @Test
    public void test_aqua_code() {
        ArrayList<Flavor> flavors = new ArrayList<>() {{
            add(globalFlavor);
            add(new Flavor(2, "lemon", "desc", 5));
        }};
        Aqua aqua = new Aqua(0, "name", flavors, PackSize.SINGLE, 0);
        assertEquals("{\"code\":\"1-2\"}", aqua.getAquaCode());
    }

    @Test
    public void new_name() {
        ArrayList<Flavor> flavors = new ArrayList<>() {{
            add(globalFlavor);
            add(new Flavor(2, "lemon", "", 5));
        }};
        
        assertEquals("blueberry-lemon-SINGLE-PACK", Aqua.generateName(flavors, PackSize.SINGLE).toString());
    }
}

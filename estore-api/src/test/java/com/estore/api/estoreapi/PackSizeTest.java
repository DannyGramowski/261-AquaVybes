package com.estore.api.estoreapi;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.aqua.PackSize;

public class PackSizeTest {

    @Test
    void testGetSize() {
        PackSize test = PackSize.SIX;
        assertSame(6, test.getSize());
    }

    @Test
    void testValueOf() {
        PackSize expected = PackSize.SIX;
        assertEquals(expected, PackSize.valueOf("SIX"));
    }

    @Test
    void testValues() {
        PackSize[] expected = new PackSize[]{PackSize.NOTVALID, PackSize.SINGLE, PackSize.SIX, PackSize.EIGHT, PackSize.TWELVE};
        assertArrayEquals(expected, PackSize.values());
    }

    @Test
    void testDeserialize() {
        String single = "SINGLE";
        String six = "SIX";
        String eight = "EIGHT";
        String twelve = "TWELVE";
        String other = "";
        assertEquals(PackSize.SINGLE, PackSize.deserialize(single));
        assertEquals(PackSize.SIX, PackSize.deserialize(six));
        assertEquals(PackSize.EIGHT, PackSize.deserialize(eight));
        assertEquals(PackSize.TWELVE, PackSize.deserialize(twelve));
        assertEquals(PackSize.NOTVALID, PackSize.deserialize(other));
    }
}

package com.estore.api.estoreapi;

import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.aqua.PackSize;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.model.user.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


public class UserTests {

    @Test
    public void testUserWithName() {
        User user1 = new User("danny");
        assertEquals(user1.getUserName().equals("danny"), true);
        assertEquals(user1.getUUIDString(), user1.getUUID().toString());
    }

    @Test
    public void testUserWithNameAndUUID() {
        UUID uuid = UUID.randomUUID();
        User user2 = new User("gavin", uuid.toString());
        assertEquals(uuid, user2.getUUID());
    }

    @Test
    public void compareUsers(){
        UUID uuid = UUID.randomUUID();
        User user2 = new User("gavin", uuid.toString());
        User user3 =  new User("gavin", uuid.toString());
        assertEquals(user2, user3);
        assertEquals("User{userName='" + user3.getUserName() +"', uuid=" + user3.getUUIDString() + "}", user3.toString());
    }

    @Test
    public void testGetCart(){
        UUID uuid = UUID.randomUUID();
        ArrayList<Aqua> cart = new ArrayList<>();
        Aqua aqua = new Aqua(0, "aqua", null, PackSize.EIGHT, 0);
        cart.add(aqua);
        User user = new User("andy", uuid.toString());
        ArrayList<Aqua> cart1 = user.getCart();
        cart1.add(aqua);
        cart1 = user.getCart();
        assertEquals(cart, cart1);
    }

    @Test
    public void testAddToCart(){
        UUID uuid = UUID.randomUUID();
        ArrayList<Aqua> cart = new ArrayList<>();
        Aqua aqua = new Aqua(0, "aqua", null, PackSize.EIGHT, 0);
        cart.add(aqua);
        User user = new User("andy", uuid.toString());
        user.addToCart(aqua);
        ArrayList<Aqua> cart1 = user.getCart();
        assertEquals(cart, cart1);
    }

    @Test
    public void testRemoveFromCart(){
        UUID uuid = UUID.randomUUID();
        ArrayList<Aqua> cart = new ArrayList<>();
        Aqua aqua = new Aqua(0, "aqua", null, PackSize.EIGHT, 0);
        User user = new User("andy", uuid.toString());
        ArrayList<Aqua> cart1 = user.getCart();
        cart1.add(aqua);
        user.removeFromCart(aqua.getId());
        assertEquals(cart, cart1);
    }


    

    @Test
    public void testCheckout(){
        UUID uuid = UUID.randomUUID();
        ArrayList<Flavor> flavors = new ArrayList<>();
        flavors.add(new Flavor(0, "cherry", "cherry desc", 5));
        Aqua aqua = new Aqua(0, "water", flavors, PackSize.SINGLE, 0);
        User user = new User("andy", uuid.toString());
        Aqua[] expected = new Aqua[]{aqua};
        user.addToCart(aqua);
        assertEquals(expected[0], user.checkout()[0]);
    }
}

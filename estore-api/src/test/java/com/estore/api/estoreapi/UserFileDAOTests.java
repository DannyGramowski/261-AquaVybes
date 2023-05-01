package com.estore.api.estoreapi;

import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.aqua.PackSize;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.model.user.User;
import com.estore.api.estoreapi.persistence.user.UserFileDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserFileDAOTests {
     static UserFileDAO dao;
     static ObjectMapper mockObjectWrapper;
     static ObjectMapper flavorMockWrapper;
     static User[] testUsers;

    @BeforeAll
    public static void init() {
        mockObjectWrapper = mock(ObjectMapper.class);
        testUsers = new User[1];
        testUsers[0] = new User("test 1");
        try {
            when(mockObjectWrapper.readValue(new File("usersTest.json"), User[].class))
                    .thenReturn(testUsers);

            dao = new UserFileDAO("usersTest.json", mockObjectWrapper);
            dao.createUser("admin");
            dao.createUser("danny");
            dao.createUser("jordan");
            dao.createUser("andy");
            dao.createUser("rafa");
            dao.createUser("gavin");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Test
    public void testCreateUser() {
        User testCreate = dao.createUser("testCreate");
        assertEquals(testCreate, dao.getUser(testCreate.getUUID()));
    }

    @Test
    public void testIsAdmin() {
        User adminUser = dao.getUser("admin");
        assertEquals(dao.isAdmin(adminUser.getUUIDString()), true);
    }

    @Test
    public void testRemoveUser() {
        dao.removeUser(dao.getUser("danny").getUUIDString());
        assertEquals(dao.getUser("danny"), null);
    }

    @Test
    public void testGetCart() throws Exception {
        String uuid = dao.getUser("andy").getUUIDString();
        assertEquals(dao.getCart(uuid), new ArrayList<>());
    }

    @Test
    public void testAddToCart() throws Exception, IOException {
        String uuid = dao.getUser("andy").getUUIDString();
        Aqua aqua = new Aqua(0, uuid, null, null, 0);
        dao.addToCart(uuid, aqua);
        ArrayList<Aqua> testCart = new ArrayList<Aqua>();
        testCart.add(aqua);
        assertEquals(dao.getCart(uuid), testCart);
    }

    @Test
    public void testRemoveFromCart() throws Exception{
        String uuid = dao.getUser("andy").getUUIDString();
        Aqua aqua = new Aqua(0, uuid, null, null, 0);
        dao.addToCart(uuid, aqua);
        assertTrue(dao.removeFromCart(uuid, aqua.getId()));
    }

    @Test 
    public void testEnoughInventory() {
        Flavor[] flavors = {
            new Flavor(0, "f1", "", 3),
            new Flavor(1, "f2", "", 10),
            new Flavor(2, "f3", "", 0)
        };

        ArrayList<Aqua> aquasList1 = new ArrayList<>();
        aquasList1.add(new Aqua(0, "0", createFlavorList(flavors[0]), PackSize.SINGLE, 0)) ;
        aquasList1.add(new Aqua(1, "1", createFlavorList(new Flavor[]{flavors[1], flavors[0]}), PackSize.SIX, 0));
        aquasList1.add(new Aqua(2, "2", createFlavorList(flavors[2]), PackSize.SINGLE, 0));
        var output = dao.enoughInventory(aquasList1, flavors, null);

        assertEquals("0,1|there is not enough stock for flavor f1", output.get(0));
        assertEquals("2|there is not enough stock for flavor f3", output.get(1));
        assertEquals(output.size(), 2);
    }

    private ArrayList<Flavor> createFlavorList(Flavor[] fs) {
        ArrayList<Flavor> flavor = new ArrayList<>();
        for(var f : fs) {
            flavor.add(f);
        }
        return flavor;
    }

    private ArrayList<Flavor> createFlavorList(Flavor f) {
        ArrayList<Flavor> flavor = new ArrayList<>();
        flavor.add(f);
        
        return flavor;
    }

    @Test
    public void testValidateFlavor() {
        Flavor[] flavors = {
                new Flavor(0, "f1", "", 3),
                new Flavor(1, "f2", "", 10),
                new Flavor(2, "f3", "", 0)
        };

        assertTrue(dao.validFlavor(flavors[0], flavors));
        assertTrue(!dao.validFlavor(new Flavor(-1, "f", "", 1), flavors));
    }

    @Test
    public void testValidateAqua() {
        Flavor[] flavors = {
                new Flavor(0, "f1", "", 3),
                new Flavor(1, "f2", "", 10),
                new Flavor(2, "f3", "", 0)
        };
        Flavor other = new Flavor(3, "f4", "", 5);

        ArrayList<Aqua> aquasList1 = new ArrayList<>();
        aquasList1.add(new Aqua(0, "0", createFlavorList(flavors[0]), PackSize.SINGLE, 0)) ;
        aquasList1.add(new Aqua(1, "1", createFlavorList(new Flavor[]{flavors[1], flavors[0]}), PackSize.SIX, 0));
        aquasList1.add(new Aqua(2, "2", createFlavorList(flavors[2]), PackSize.SINGLE, 0));

        assertTrue(dao.validateAqua(new Aqua(1, "1", createFlavorList(new Flavor[]{flavors[1], flavors[0]}), PackSize.SIX, 0), flavors).size() == 0); //no errors

        assertTrue(dao.validateAqua(new Aqua(1, "1", createFlavorList(new Flavor[]{flavors[1], other}), PackSize.SIX, 0), flavors).size() > 0);


    }

    @Test
    public void testValidateCart() {
        Flavor[] flavors = {
                new Flavor(0, "f1", "", 3),
                new Flavor(1, "f2", "", 10),
                new Flavor(2, "f3", "", 0)
        };
        Flavor other = new Flavor(3, "f4", "", 5);

        ArrayList<Aqua> aquasList1 = new ArrayList<>();
        aquasList1.add(new Aqua(0, "0", createFlavorList(flavors[0]), PackSize.SINGLE, 0)) ;
        aquasList1.add(new Aqua(1, "1", createFlavorList(new Flavor[]{flavors[1], flavors[0]}), PackSize.SIX, 0));
        aquasList1.add(new Aqua(2, "2", createFlavorList(flavors[2]), PackSize.SINGLE, 0));
        aquasList1.add(new Aqua(3, "3", createFlavorList(new Flavor[]{other}), PackSize.SIX, 0));

        User jordan = dao.getUser("jordan");
        for(Aqua a : aquasList1) {
            jordan.addToCart(a);
        }
        var result = dao.validateInventory(jordan.getUUIDString(), flavors, null);

        assertEquals(result[0], "3| flavor f4 is not a valid flavor");
        assertEquals(result[1], "0,1|there is not enough stock for flavor f1");
        assertEquals(result[2], "2|there is not enough stock for flavor f3");
        assertTrue(result.length == 3);

        for(Aqua a : aquasList1) {
            jordan.removeFromCart(a.getId());
        }
//        3| flavor f4 is not a valid flavor
//        0,1|there is not enough stock for flavor f1
//        2|there is not enough stock for flavor f3
    }
    
    @Test
    public void testPack() {
        String userid = dao.getUser("danny").getUUIDString();
        assertEquals(dao.getCurrentPack(userid), null);
        Aqua a1 = new Aqua(1, "test1", null, PackSize.SINGLE, 0);
        Aqua a2 = new Aqua(2, "test2", null, PackSize.SINGLE, 0);
        Aqua a3 = new Aqua(3, "test3", null, PackSize.SINGLE, 0);
        Aqua a4 = new Aqua(4, "test4", null, PackSize.SINGLE, 0);
        Aqua a5 = new Aqua(5, "test5", null, PackSize.SINGLE, 0);
        Aqua a6 = new Aqua(6, "test6", null, PackSize.SINGLE, 0);
        Aqua a7 = new Aqua(7, "test7", null, PackSize.SINGLE, 0);
        Aqua[] arr = {a1, a2, a3, a4, a5, a6};
        dao.addToPack(userid, a1);
        Aqua gottenAqua = dao.getCurrentPack(userid).get(0);
        assertEquals(a1, gottenAqua);
        assertTrue(gottenAqua.getOrderID() < 0);

        dao.removeFromPack(userid, a1.getId());
        assertEquals(null, dao.getCurrentPack(userid));
        
        for(Aqua a : arr) {
            dao.addToPack(userid, a);
        }
        assertEquals(dao.getCurrentPack(userid), dao.getPack(userid, a1.getOrderID()));

        ArrayList<Flavor> flavors = new ArrayList<>();
        flavors.add(new Flavor(0, "cherry", "", 5));
        flavors.add(new Flavor(1, "lemon", "", 5));
        dao.editAquaInPack(userid, a1.getId(), new Aqua(55, "an edited aqua", flavors, null, 0));

        assertTrue(a1.getId() == 1 &&
        a1.getName().equals("an edited aqua") &&
        a1.getFlavors().equals(flavors) &&
        a1.getPackSize() == PackSize.SINGLE &&
        a1.getOrderID() != 0);

        dao.addToPack(userid, a7);

        assertNotEquals(dao.getCurrentPack(userid), dao.getPack(userid, a1.getOrderID()));
        assertEquals(dao.getCurrentPack(userid).get(0), a7);
        assertTrue(a1.getOrderID() == a2.getOrderID() && a2.getOrderID() == a6.getOrderID() && a6.getOrderID() != a7.getOrderID());
    }

    @Test
    public void testCheckout() throws IOException, Exception{
        String uuid = dao.getUser("andy").getUUIDString();
        ArrayList<Flavor> flavors = new ArrayList<>();
        flavors.add(new Flavor(0, "cherry", "cherry desc", 5));
        Aqua aqua = new Aqua(0, "water", flavors, PackSize.SINGLE, 0);
        dao.addToCart(uuid, aqua);
        Aqua[] testArr = {aqua};
        System.out.println(dao.getCart(uuid));
        System.out.println(testArr);
        assertEquals(dao.checkout(uuid)[0], testArr[0]);
    }
}

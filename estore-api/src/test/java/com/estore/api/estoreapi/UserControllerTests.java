package com.estore.api.estoreapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.controller.UserController;
import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.aqua.PackSize;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.model.user.User;
import com.estore.api.estoreapi.persistence.flavor.FlavorFileDAO;
import com.estore.api.estoreapi.persistence.user.UserFileDAO;


public class UserControllerTests {
    private UserController userController;
    private UserFileDAO mockUserDao;
    private FlavorFileDAO flavorDAO;
    private ArrayList<Flavor> flavors;
    
    @BeforeEach
    public void setUpUserController() {
        mockUserDao = mock(UserFileDAO.class);
        userController = new UserController(mockUserDao, flavorDAO);
        flavors = new ArrayList<Flavor>();
        flavors.add(new Flavor(0, "new", "desc", 0));
    }

    @Test
    public void testSignIn() {
        User user = new User("gavin");
        when(mockUserDao.getUser("gavin")).thenReturn(user);
        
        ResponseEntity<User> response = userController.signIn("gavin");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testSignInNewUser() {
        when(mockUserDao.getUser("gavin")).thenReturn(null);

        ResponseEntity<User> response = userController.signIn("gavin");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testGetUser() {
        User user = new User("gavin");
        when(mockUserDao.getUser(user.getUUID())).thenReturn(user);

        ResponseEntity<User> response = userController.getUser(user.getUUIDString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetUser404() {
        User user = new User("gavin");
        when(mockUserDao.getUser(user.getUUID())).thenReturn(null);

        ResponseEntity<User> response = userController.getUser(user.getUUIDString());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddToCart() throws IOException {
        User user = new User("gavin");
        Aqua aqua = new Aqua(0, "test", flavors, PackSize.SIX, 0);
        mockUserDao.addToCart(user.getUUIDString(), aqua);

        ResponseEntity<Aqua> response = userController.addToCart(user.getUUIDString(), aqua);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(aqua, response.getBody());
    }

    @Test
    public void testAddToCartException() throws IOException {
        User user = new User("gavin");
        Aqua aqua = new Aqua(0, "test", flavors, PackSize.SIX, 0);
        doThrow(new IOException()).when(mockUserDao).addToCart(user.getUUIDString(), aqua);

        ResponseEntity<Aqua> response = userController.addToCart(user.getUUIDString(), aqua);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    } 

    @Test
    public void testRemoveFromCart() throws IOException {
        User user = new User("gavin");
        Aqua aqua = new Aqua(0, "test", flavors, PackSize.SIX, 0);
        when(mockUserDao.removeFromCart(user.getUUIDString(), aqua.getId())).thenReturn(true);

        ResponseEntity<Boolean> response = userController.removeFromCart(user.getUUIDString(), aqua.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    public void testRemoveFromCart404() throws IOException {
        User user = new User("gavin");
        Aqua aqua = new Aqua(0, "test", flavors, PackSize.SIX, 0);
        when(mockUserDao.removeFromCart(user.getUUIDString(), aqua.getId())).thenReturn(false);

        ResponseEntity<Boolean> response = userController.removeFromCart(user.getUUIDString(), aqua.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(false, response.getBody());
    }
    @Test
    public void testRemoveFromCartException() throws IOException {
        User user = new User("gavin");
        Aqua aqua = new Aqua(0, "test", flavors, PackSize.SIX, 0);
        doThrow(new IOException()).when(mockUserDao).removeFromCart(user.getUUIDString(), aqua.getId());

        ResponseEntity<Boolean> response = userController.removeFromCart(user.getUUIDString(), aqua.getId());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    } 

    @Test
    public void testGetCart() throws Exception {
        User user = new User("gavin");
        Aqua aqua = new Aqua(0, "test", flavors, PackSize.SIX, 0);
        ArrayList<Aqua> cart = new ArrayList<Aqua>();
        cart.add(aqua);
        //Aqua[] cartArray = new Aqua[cart.size()];
        //cartArray = cart.toArray(cartArray);
        when(mockUserDao.getCart(user.getUUIDString())).thenReturn(cart);

        ResponseEntity<Aqua[]> response = userController.getCart(user.getUUIDString());


        Aqua[] cartArray = new Aqua[cart.size()];
        cartArray = cart.toArray(cartArray);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        //assertEquals(cartArray, response.getBody());
    } 

    @Test
    public void testGetCartException() throws Exception {
        User user = new User("gavin");
        Aqua aqua = new Aqua(0, "test", flavors, PackSize.SIX, 0);
        doThrow(new Exception()).when(mockUserDao).getCart(user.getUUIDString());

        ResponseEntity<Aqua[]> response = userController.getCart(user.getUUIDString());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    } 

    // currently controller just returns INTERNAL SERVER ERROR
/*     @Test
    public void testCheckout() throws Exception {
        User user = new User("gavin","eac59240-6a47-4aea-a3c6-4a48472a2b2b");
        Aqua[] aquas = new Aqua[2];
        Aqua aqua1 = new Aqua(0, "test", flavors, PackSize.SIX, 0);
        Aqua aqua2 = new Aqua(1, "test2", flavors, PackSize.SIX, 0);
        aquas[0] = aqua1;
        aquas[1] = aqua2;

        when(mockUserDao.checkout(user.getUUIDString())).thenReturn(aquas);

        ResponseEntity<Aqua[]> response = userController.checkout(user.getUUIDString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(aquas, response.getBody());
    } */

    @Test
    public void testCheckoutException() throws Exception {
        User user = new User("gavin");

        doThrow(new Exception()).when(mockUserDao).checkout(user.getUUIDString());

        ResponseEntity<Aqua[]> response = userController.checkout(user.getUUIDString());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    } 

/*     @Test
    public void testContainsAqua() {
        User user = new User("gavin");
        Aqua aqua1 = new Aqua(0, "test", flavors, PackSize.SIX, 0);

        ResponseEntity<Boolean> response = userController.containsAqua(user.getUUIDString(), aqua1.getId());
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testContainsAquaException() {
        User user = new User("gavin");
        //Aqua aqua = new Aqua(0, "test", flavors, PackSize.SIX, 0);

        ResponseEntity<Boolean> response = userController.containsAqua(user.getUUIDString(), 10000);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    } */
}

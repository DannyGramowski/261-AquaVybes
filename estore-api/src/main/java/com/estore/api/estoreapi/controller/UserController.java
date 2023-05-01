package com.estore.api.estoreapi.controller;

import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.aqua.AquaValidateData;
import com.estore.api.estoreapi.model.aqua.PackSize;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.model.user.User;
import com.estore.api.estoreapi.persistence.flavor.FlavorDAO;
import com.estore.api.estoreapi.persistence.user.UserDAO;
import com.estore.api.estoreapi.persistence.user.UserFileDAO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("user")
public class UserController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private static UserDAO dao;
    private static FlavorDAO flavordao;
    
    public UserController(UserDAO userDAO, FlavorDAO flvrdao) {
        dao = userDAO;
        flavordao = flvrdao;
    }

    @GetMapping("/signin/{username}")
    public ResponseEntity<User> signIn(@PathVariable String username) {
        User user = dao.getUser(username);
        if(user == null) {
            user = dao.createUser(username);
            LOG.info("Get/user/signin/" + username + " created");
            return new ResponseEntity<User>(user, HttpStatus.CREATED);
        } else {
            LOG.info("Get/user/signin/" + username);
            return new ResponseEntity<User>(user, HttpStatus.OK);

        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<User> getUser(@PathVariable String uuid) {
        LOG.info("Get/user/" + uuid);

        User user = dao.getUser(UUID.fromString(uuid));
        if(user != null) {
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * adds the aqua to the cart of given uuid
     * @param uuid uuid number of cart
     * @param aqua aqua to add
     * @return the aqua added to the cart
     */
    @PutMapping("/cart")
    public ResponseEntity<Aqua> addToCart(@RequestParam String uuid, @RequestBody Aqua aqua){
        LOG.info("PUT/?uuid=" + uuid + "/cart/");
        try {
            dao.addToCart(uuid, aqua);
            return new ResponseEntity<Aqua>(aqua, HttpStatus.OK);
        }
        catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * removes the aqua from the cart of given uuid
     * @param uuid uuid number of cart
     * @param aqua aqua to remove
     * @return the aqua removed from the cart
     */
    @DeleteMapping("/cart")
    public ResponseEntity<Boolean> removeFromCart(@RequestParam String uuid, @RequestParam int id){
        LOG.info("DELETE/?uuid=" + uuid + "/cart/?id=" + id);
        try {
            boolean bool = dao.removeFromCart(uuid, id);
            if (bool){
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** 
     * gets cart from given uuid
     * @param uuid uuid of cart
     * @return list of all Aquas in the cart
     */
    @GetMapping("/cart")
    public ResponseEntity<Aqua[]> getCart(@RequestParam String uuid) {
        LOG.info("GET/?uuid=" + uuid + "/cart");
        try {
            ArrayList<Aqua> cart = dao.getCart(uuid);
            Aqua[] arr = new Aqua[cart.size()];
            arr = cart.toArray(arr); 
            return new ResponseEntity<Aqua[]>(arr, HttpStatus.OK);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * checks out cart, deleting everything
     * @param uuid uuid of cart
     * @return list of all Aquas in the cart
     */
    @GetMapping("checkout/{uuid}")
    public ResponseEntity<Aqua[]> checkout(@PathVariable String uuid){
        LOG.info("GET/?uuid=" + uuid + "/checkout");
        try {
            User user = dao.getUser(UUID.fromString(uuid));
            ArrayList<Aqua> tempCart = new ArrayList<>(user.getCart()) ;
            tempCart.addAll(user.getPacks());
            var result = dao.enoughInventory(tempCart, flavordao.getAllFlavors(), null);
            if(result.size() != 0) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            flavordao.removeFromInventory(tempCart);
            Aqua[] cart = dao.checkout(uuid);
            return new ResponseEntity<Aqua[]>(cart, HttpStatus.OK);
        }
        catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/validate/userinventory/{uuid}")
    public ResponseEntity<String[]> validateInventory(@PathVariable String uuid, @RequestParam String flavorString) {
        LOG.info("GET/validate/userinventory/" + uuid);
        try {
            ArrayList<Flavor> aquaFlavors = parseFlavors(flavorString);
            Flavor[] arr = new Flavor[aquaFlavors.size()];
            arr = aquaFlavors.toArray(arr);
            return new ResponseEntity<>(dao.validateInventory(uuid, arr,null), HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/validate/userinventoryandaqua/{uuid}/{aquaFlavorString}")
    public ResponseEntity<String[]> validateInventoryWithNewAqua(@PathVariable String uuid, @PathVariable String aquaFlavorString,
    @RequestParam String flavorString, @RequestParam PackSize aquaPackSize) {
        LOG.info("GET/validate/userinventoryandaqua/" + uuid);
        try {
            // ArrayList<Flavor> aquaFlavors = parseFlavors(flavorString);
            // Flavor[] arr = new Flavor[aquaFlavors.size()];
            // arr = aquaFlavors.toArray(arr);
            // System.out.println("aqua flavors " + aquaFlavors);
            Flavor[] flavors = flavordao.getAllFlavors();
//            System.out.println("new aqua " + aqua);
            var data = new AquaValidateData(parseFlavors(aquaFlavorString), aquaPackSize);
            return new ResponseEntity<>(dao.validateInventory(uuid, flavors, data), HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/validate/packsize/{uuid}")//need flavors and packsize
    public ResponseEntity<Boolean> validateInventory(@PathVariable String uuid, @RequestParam PackSize packSize, @RequestParam String flavorString) throws Exception {
        LOG.info("GET/validate/packsize/"+uuid+"?"+packSize+" " + flavorString);

        try {
            ArrayList<Aqua> aquas = new ArrayList<>(dao.getCart(uuid));
            aquas.addAll(dao.getAllPacks(uuid));
            ArrayList<Flavor> aquaFlavors = parseFlavors(flavorString);
            if(!flavorString.equals("-1")) aquas.add(new Aqua(-1, uuid, aquaFlavors, packSize, 0));

            ArrayList<String> result = dao.enoughInventory(aquas, flavordao.getAllFlavors(), null);
            for(int i = 0; i < result.size(); i++) {
                var arr = result.get(i).split(" ");
                result.set(i, arr[arr.length-1]);

            }
            for(Flavor flavor : aquaFlavors) {
                if(result.contains(flavor.getName())) return ResponseEntity.ok(false);
            }
            return ResponseEntity.ok(true);

        } catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ArrayList<Flavor> parseFlavors(String flavorString) throws IOException{
        ArrayList<Flavor> aquaFlavors = new ArrayList<>();
        if(!flavorString.equals("-1")) {
            String[] flavorIds = flavorString.split(",");
            for (String str : flavorIds) {
                aquaFlavors.add(flavordao.getFlavor(Integer.parseInt(str)));
            }
        }
        return aquaFlavors;
    }

    @GetMapping("contains/{uuid}/{id}")
    public ResponseEntity<Boolean> containsAqua(@PathVariable String uuid, @PathVariable int id) {
        LOG.info("GET/?uuid=" + uuid + "/contains");
        try {
            boolean contains = dao.getCart(uuid).stream().filter(a -> a.getId() == id).toArray().length != 0;
            return ResponseEntity.ok(contains);
        } catch (Exception e) {
            
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);        }

    }

    @GetMapping("pastOrders/{uuid}")
    public ResponseEntity<Aqua[]> getPastOrders(@PathVariable String uuid) {
        LOG.info("GET/pastOrders/" + uuid);
        return ResponseEntity.ok(dao.getPastOrders(uuid));
    }
}

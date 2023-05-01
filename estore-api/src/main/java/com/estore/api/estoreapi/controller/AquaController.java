package com.estore.api.estoreapi.controller;

import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.persistence.aqua.AquaDAO;
import com.estore.api.estoreapi.services.FlavorNotFound;
import com.estore.api.estoreapi.services.FlavorService;

import org.apache.juli.logging.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("aquas")
public class AquaController {
    private static final Logger LOG = Logger.getLogger(AquaController.class.getName());
    private final AquaDAO aquaDAO;
    private final FlavorService flavorService;


    /**
     * Creates a new RestController
     * @param aquaDAO the DAO that the control will pull from
     */
    public AquaController(AquaDAO aquaDAO, FlavorService flavorService) {
        this.aquaDAO = aquaDAO;
        this.flavorService = flavorService;
    }

    /**
     * @return an array of every item in the inventory
     */
    @GetMapping("")
    public ResponseEntity<Aqua[]> getInventory() {
        LOG.info("GET/aquas/");
        try {
            return new ResponseEntity<Aqua[]>(aquaDAO.getInventory(), HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @PostMapping("")
    public ResponseEntity<Aqua> createAqua(@RequestBody Aqua aqua) {
        LOG.info("POST/aquas" + aqua);
        try {
            Aqua newAqua = aquaDAO.createAqua(aqua.getName(),  aqua.getFlavors(), aqua.getPackSize(), 0);
            if(newAqua != null) {
                return new ResponseEntity<Aqua>(newAqua, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("")
    public ResponseEntity<Aqua> updateAqua(@RequestBody Aqua aqua) {
        LOG.info("Put/aquas" + aqua);
        try {
            Aqua newAqua = aquaDAO.updateAqua(aqua.getId(), aqua.getName(),  aqua.getFlavors(), aqua.getPackSize());
            if(newAqua != null) {
                return new ResponseEntity<Aqua>(newAqua, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    

    /**
     * gets the aqua based off of the numeric id
     * @param id id numers
     * @return the aqua with the given id if it exists, otherwise HttpStatus.NOT_FOUND
     */
    @GetMapping("/{id}")
    public ResponseEntity<Aqua> getAquaById(@PathVariable int id){
        LOG.info("GET/aquas/" + id);
        try {
            Aqua aqua = aquaDAO.getAqua(id);
            if (aqua != null)
                return new ResponseEntity<Aqua>(aqua,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * deletes the aqua with the given id
     * @param id id of the aqua you want to delete
     * @return the aqua you deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteAquaById(@PathVariable int id){
        LOG.info("DELETE/aquas/" + id);
        try {
            boolean bool = aquaDAO.deleteAqua(id);
            if (bool)
                return new ResponseEntity<>( HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * searches the inventory for all aquas that meet that name requirement
     * @param name name you are searching for
     * @return aquas that match the given name
     */
    @GetMapping("/search/name/{name}")
    public ResponseEntity<Aqua[]> searchAquasByName(@PathVariable String name) {
        LOG.info("GET/aquas/search/name/?name="+name);
        try {
            Aqua[] aquas = aquaDAO.searchInventory(name);
            if (aquas != null){
                return new ResponseEntity<Aqua[]>(aquas, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * searches the inventory for all aquas that meet that flavor requirement
     * @param flavor flavor you are searching for
     * @return aquas that match the given flavor
     */
    @GetMapping("/search/flavor/{flavor}")
    public ResponseEntity<Aqua[]> searchAquas(@PathVariable Flavor flavor) {
        LOG.info("GET /heroes/?flavor="+flavor.getName());
        try {
            Aqua[] aquas = aquaDAO.searchInventory(flavor);
            if (aquas != null){
                return new ResponseEntity<Aqua[]>(aquas, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Aqua> getByCode(@PathVariable String code) {
        LOG.info("GET /code/"+code);
        try {
            Aqua aqua = aquaDAO.getAqua(Integer.parseInt(code, 16));
            if (aqua != null){
                return new ResponseEntity<Aqua>(aqua, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/code")
    public ResponseEntity<String> postCode(@RequestBody Aqua aqua) {
        LOG.info("POST/aquas/code, code=" + aqua.getAquaCode());
        LOG.info(aqua.toString());
        try {
            if (aqua.getFlavors() != null) {
                Aqua newAqua = aquaDAO.createAqua(aqua.getName(), aqua.getFlavors(), aqua.getPackSize(), -1);
                LOG.info(String.format("{\"code\":\"%x\"}", newAqua.getId()));
                return new ResponseEntity<String>(String.format("{\"code\":\"%x\"}", newAqua.getId()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

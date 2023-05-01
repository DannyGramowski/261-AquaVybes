package com.estore.api.estoreapi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.catalina.connector.Response;
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

import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.persistence.flavor.FlavorDAO;


@RestController
@RequestMapping("flavors")
public class FlavorController {
    private static final Logger LOG = Logger.getLogger(FlavorController.class.getName());
    private static FlavorDAO dao;

    public FlavorController(FlavorDAO dao) {
        this.dao = dao;
    }

    /** 
     * gets all flavors
     * @return all flavors if there is no error accessing the file
     */
    @GetMapping("")
    public ResponseEntity<Flavor[]> getFlavors() {
        LOG.info("GET/flavors/");
        try {
            return new ResponseEntity<Flavor[]>(dao.getAllFlavors(), HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("search/{name}")
    public ResponseEntity<Flavor[]> searchFlavors(@PathVariable String name) {
        LOG.info("GET/flavors/search/" + name);
        var flavors = dao.searchFlavors(name);
        return new ResponseEntity<Flavor[]>(flavors, HttpStatus.OK);
    }

    /**
     * gets the flavor based off of the numeric id
     * @param id id number
     * @return the flavor with the given id if it exists, otherwise HttpStatus.NOT_FOUND
     */
    @GetMapping("/{id}")
    public ResponseEntity<Flavor> getFlavorById(@PathVariable int id){
        LOG.info("GET/flavors/" + id);
        try {
            Flavor flavor = dao.getFlavor(id);
            if (flavor != null)
                return new ResponseEntity<Flavor>(flavor,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<Flavor> createFlavor(@RequestBody Flavor flavor) {
        LOG.info("POST/flavors" + flavor);
        try {
            Flavor newFlavor = dao.createFlavor(flavor.getName(), flavor.getDescription(), flavor.getQuantity());
            if(newFlavor != null) {
                return new ResponseEntity<Flavor>(newFlavor, HttpStatus.OK);
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
     * deletes the flavor with the given id
     * @param id id of the flavor you want to delete
     * @return the flavor you deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteFlavorById(@PathVariable int id){
        LOG.info("DELETE/flavors/" + id);
        try {
            boolean bool = dao.deleteFlavor(id);
            if (bool)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * updates using given flavor
     * @param flavor updated flavor object
     * @return OK if successful, CONFLICT otherwise
     */
    @PutMapping("")
    public ResponseEntity<Flavor> updateFlavor(@RequestBody Flavor flavor) {
        LOG.info("Put/flavors" + flavor);
        try {
            Flavor newFlavor = dao.updateFlavor(flavor.getId(),
                                                flavor.getName(),
                                                flavor.getDescription(),
                                                flavor.getQuantity());
                                                
            if(newFlavor != null) {
                return new ResponseEntity<Flavor>(newFlavor, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}

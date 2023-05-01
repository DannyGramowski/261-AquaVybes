package com.estore.api.estoreapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.persistence.user.UserDAO;
import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.aqua.PackSize;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.model.user.User;

@RestController
@RequestMapping("/pack")
public class PackController {
    private static final Logger LOG = Logger.getLogger(AquaController.class.getName());
    
    public static final Aqua packTerminater;
    static {
        ArrayList<Flavor> flavors = new ArrayList<Flavor>();
        flavors.add(new Flavor(-1,"notvalid", "", -1));
        packTerminater = new Aqua(0, "terminator", flavors, PackSize.NOTVALID, 0);
    }
    UserDAO dao;

    public PackController(UserDAO dao) {
        this.dao = dao;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Aqua[]> getCurrentPack(@PathVariable String uuid) {
        LOG.info("GET/pack/" + uuid);
        try {
            ArrayList<Aqua> pack = dao.getCurrentPack(uuid);
            if(pack == null) { //if it is null that means that it is a new pack which is fine but you cant send null in a response entity
                Aqua[] arr = {};
                return new ResponseEntity<Aqua[]>(arr, HttpStatus.OK);
            } else {
                Aqua[] arr = new Aqua[pack.size()];
                arr = pack.toArray(arr);
                return new ResponseEntity<Aqua[]>(arr, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{uuid}/{packID}")
    public ResponseEntity<Aqua[]> getPack(@PathVariable String uuid, @PathVariable int packID) {
        LOG.info("GET/pack/" + uuid + "/" + packID);
        try {
            ArrayList<Aqua> pack = dao.getPack(uuid, packID);
            if(pack == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                Aqua[] arr = new Aqua[pack.size()];
                arr = pack.toArray(arr);
                return new ResponseEntity<Aqua[]>(arr, HttpStatus.OK);
            }
        } catch(Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<Aqua[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all/{uuid}")
    public ResponseEntity<Aqua[]> getAllPacks(@PathVariable String uuid){
        ArrayList<Aqua> aquas = dao.getAllPacks(uuid);
        Aqua[] arr = new Aqua[aquas.size()];
        arr = aquas.toArray(arr);
        return new ResponseEntity<Aqua[]>(arr, HttpStatus.OK);
    }

    @GetMapping("/contains/{uuid}/{id}")
    public ResponseEntity<Boolean> containsAqua(@PathVariable String uuid, @PathVariable int id) {
        LOG.info("GET/?uuid=" + uuid + "/contains");
        try {
            boolean contains = false;
            if(dao.getCurrentPack(uuid) == null) {
                return ResponseEntity.ok(false);
            }
            for(Aqua a : dao.getCurrentPack(uuid)) {
                if(a.getId() == id) contains = true;
            }

            return ResponseEntity.ok(contains);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);        }
    }

    @GetMapping("/submitpack/{uuid}")
    public ResponseEntity<Aqua> submitPack(@PathVariable String uuid) {
        LOG.info("GET/submitpack/" + uuid);
            if(dao.getCurrentPack(uuid) != null) {
                dao.addToPack(uuid, packTerminater);
                return new ResponseEntity<Aqua>(packTerminater, HttpStatus.OK);
            }
            return new ResponseEntity<Aqua>(packTerminater, HttpStatus.NOT_ACCEPTABLE);
        }

    @PostMapping("/{uuid}")
    public ResponseEntity<Aqua> addToPack(@PathVariable String uuid, @RequestBody Aqua aqua) {
        LOG.info("POST/pack/" + uuid);
        try {
            Aqua addedAqua = dao.addToPack(uuid, aqua);
            return new ResponseEntity<Aqua>(addedAqua, HttpStatus.CREATED);

        } catch(Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<Aqua>(HttpStatus.INTERNAL_SERVER_ERROR);
        }        
    }

    @PutMapping("/{uuid}/{aquaID}")
    public ResponseEntity<Aqua> editAquaInPack(@PathVariable String uuid, @PathVariable int aquaID, @RequestBody Aqua newAqua) {
        LOG.info("PUT/pack/" + uuid + "/" + aquaID);
        try {
            Aqua editedAqua = dao.editAquaInPack(uuid, aquaID, newAqua);
            if(editedAqua == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<Aqua>(editedAqua, HttpStatus.OK);
            }
        } catch(Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<Aqua>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{uuid}/{aquaID}")
    public ResponseEntity<Aqua> removeFromPack(@PathVariable String uuid, @PathVariable int aquaID) {
        LOG.info("DELETE/pack/" + uuid + "/" + aquaID);
        try {
            Aqua deletedAqua = dao.removeFromPack(uuid, aquaID);
            if(deletedAqua == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<Aqua>(deletedAqua, HttpStatus.ACCEPTED);
            }
        } catch(Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<Aqua>(HttpStatus.INTERNAL_SERVER_ERROR);
        }        
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Aqua[]> deletePack(@PathVariable String uuid, @RequestParam int packID) {
        LOG.info("DELETE/pack/" + uuid + "?packID=" + packID);
        ArrayList<Aqua> aquas = dao.deletePack(uuid, packID);
        Aqua[] result = new Aqua[aquas.size()];
        result = aquas.toArray(result);
        return new ResponseEntity<Aqua[]>(result, HttpStatus.ACCEPTED);
    }
}

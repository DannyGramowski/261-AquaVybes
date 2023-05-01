package com.estore.api.estoreapi.persistence.aqua;

import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.model.aqua.PackSize;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AquaFileDAO implements AquaDAO{
    private ArrayList<Aqua> inventory;

    private ObjectMapper objectMapper;

    private String filename;
    private static Integer nextId;

    public AquaFileDAO(@Value("${inventory.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        loadInventory();
    }
    
    /**
     * {@inheritDoc}}
     */
    public Aqua createAqua(String name, ArrayList<Flavor> flavors, PackSize packSize, int orderID) throws IOException{
        
        String finalName;
        if (name.equals("DEFAULT")) {
            StringBuilder newName = Aqua.generateName(flavors, packSize);
            finalName = newName.toString();
        } else {finalName = name;}


        Aqua newAqua = new Aqua(getNewId(), finalName, flavors, packSize, orderID);
        inventory.add(newAqua);
        saveInventory();

        return newAqua;
    }

    /**
     * {@inheritDoc}
     */
    public Aqua getAqua(int id) throws IOException {
        synchronized (inventory) {
            for (Aqua a : inventory) {
                if (a.getId() == id) {
                    return a;
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Aqua getAqua(String name) {
        synchronized (inventory) {
            for (Aqua a : inventory) {
                if (a.getName().equals(name)) {
                    return a;
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Aqua[] getInventory() throws IOException {
        synchronized(inventory) {
            return getInventoryArray("");
        }
    }

    /**
     * Creates array of Aqua from the inventory ArrayList that contains the targetText. 
     * If targetText is null, the entire inventory is returned.
     * @param targetText text to search for, keep null to return entire inventory
     * @return an array that represents the inventory of the store
     */
    private Aqua[] getInventoryArray(String targetText) {
        if(targetText == null) targetText = "";
        ArrayList<Aqua> targetInventory = new ArrayList<Aqua>();
        for (Aqua a : inventory) {
            if(a.getName().contains(targetText) || targetText.equals("")) {
                targetInventory.add(a);
            }
        }
        Aqua[] filteredInventory = new Aqua[targetInventory.size()];
        targetInventory.toArray(filteredInventory);
        return filteredInventory;
    }
    /**
     * {@inheritDoc}
     */
    public boolean deleteAqua(int id) throws IOException {
        synchronized (inventory) {
            for (Aqua a : inventory) {
                if (a.getId() == id) {
                    inventory.remove(a);
                    saveInventory();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}}
    */
    public Aqua updateAqua(int id, String name, ArrayList<Flavor> flavors, PackSize packSize) throws IOException {
        synchronized (inventory) {
            for ( int i = 0; i < inventory.size(); ++i ) {
                Aqua aqua = inventory.get(i);

                // If desired id is not found, continue
                if ( aqua.getId() != id ) {
                    continue;
                }

                // Update flavors if provided
                if ( !(flavors == null
                        || flavors.isEmpty()
                        || aqua.getFlavors().equals(flavors)) ) {

                    aqua.setFlavors(flavors);
                }

                // update packSize if provided
                if ( !(packSize == null
                        || aqua.getPackSize().equals(packSize)) ) {

                    aqua.setPackSize(packSize);
                }

                // Update name if provided
                if ( !(name == null 
                        || name.isEmpty()
                        || aqua.getName().equals(name)) ) {

                    aqua.setName(name.isEmpty() 
                                    ? Aqua.generateName(aqua.getFlavors(), aqua.getPackSize()).toString()
                                    : name);
                }

                saveInventory();
                // return updated aqua
                return aqua;
            }
        }

        return null;
    }

    /**
     * gets the next valid ID.
     * @return the next valid ID
     */
    private static int getNewId() {
        synchronized(nextId) {
            return ++nextId;
        }
    }

    private static void setNextId(int id) {
        synchronized(nextId) {
            if (id >= nextId) nextId = id + 1;
        }
    }

    /**
     * loads inventory from file into inventory
     * @return true if no file errors occur
     * @throws IOException
     */
    private boolean loadInventory() throws IOException {
        inventory = new ArrayList<Aqua>();
        nextId = 0;

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Aqua.class, new AquaDeserializer());
        objectMapper.registerModule(module);
        Aqua[] aquaArray = objectMapper.readValue(new File(filename), Aqua[].class);
        
        for(Aqua a: aquaArray) {
            inventory.add(a);
            if(a.getId() >= nextId) {//sets next ID to the largest id
                setNextId(a.getId());
            }
        }

        return true;
    }

    /**
     * saves current inventory representation back to json file format
     * @return true if no file error
     * @throws IOException
     */
    private boolean saveInventory() throws IOException {
        Aqua[] aquaArray = getInventoryArray("");
        SimpleModule module = new SimpleModule();
        module.addSerializer(Aqua.class, new AquaSerializer());
        objectMapper.registerModule(module);
        objectMapper.writeValue(new File(filename), aquaArray);
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    public Aqua[] searchInventory(String query) throws IOException{
        ArrayList<Aqua> list = new ArrayList<Aqua>();
        for (Aqua a : inventory){
            if (query == null || a.getName().contains(query)){
                list.add(a);
            }
        }
        Aqua[] arr = new Aqua[list.size()];
        list.toArray(arr);
        return arr;
        }

    public Aqua[] searchInventory(Flavor flavor) throws IOException{
        ArrayList<Aqua> list = new ArrayList<Aqua>();
        for (Aqua a : inventory){
            if (flavor == null || a.getFlavors().contains(flavor)){
                list.add(a);
            }
        }
        Aqua[] arr = new Aqua[list.size()];
        list.toArray(arr);
        return arr;
        }
}

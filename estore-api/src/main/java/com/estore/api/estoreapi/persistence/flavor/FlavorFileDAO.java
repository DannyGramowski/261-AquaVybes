package com.estore.api.estoreapi.persistence.flavor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Component
public class FlavorFileDAO implements FlavorDAO {
    private ArrayList<Flavor> flavors;

    private String filename;

    private ObjectMapper objectMapper;
    private static Integer nextId;


    public FlavorFileDAO(@Value("${flavors.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        loadFlavors();
    }

    /**
     * {@inheritDoc}
     */
    public Flavor createFlavor(String flavorName, String flavorDescription, int flavorQuantity) {
        Flavor newFlavor = new Flavor(getNewId(), flavorName, flavorDescription, flavorQuantity);
        flavors.add(newFlavor);
        try {
            saveFlavors();
        } catch (IOException e) {
            System.out.println(e);
            throw new Error();
        }
        return newFlavor;
    }

    public void removeFromInventory(ArrayList<Aqua> aquas) {
        HashMap<Integer, Flavor> flavorMap = new HashMap<>(); 
        for(Flavor f : flavors) {
            flavorMap.put(f.getId(), f);
        }
        for(Aqua a : aquas) {
            for(Flavor f : a.getFlavors()) {
                Flavor fileFlavor = flavorMap.get(f.getId());
                fileFlavor.setQuantity(fileFlavor.getQuantity() - a.getPackSize().getSize());
            }
        }

        try {
            saveFlavors();
        } catch(IOException e) {
            System.out.println("weeeeeeeee");
        }
    }

    /**
     * {@inheritDoc}
     */
    public Flavor[] getAllFlavors() {
        synchronized(flavors) {
            return getFlavorArray("");
        }
    }

    /** 
     * get an array of flavors based on target name
     * @param targetFlavor the name of the flavor to search, "" if all
     * @returns filtered array of flavors
     */
    private Flavor[] getFlavorArray(String targetFlavor) {
        if(targetFlavor == null) targetFlavor = "";
        ArrayList<Flavor> foundFlavors = new ArrayList<Flavor>();
        for (Flavor f : flavors) {
            if(f.getName().contains(targetFlavor) || targetFlavor.equals("")) {
                foundFlavors.add(f);
            }
        }
        Flavor[] filteredFlavors = new Flavor[foundFlavors.size()];
        foundFlavors.toArray(filteredFlavors);
        return filteredFlavors;
    }

    /**
     * {@inheritDoc}
     */
    public Flavor getFlavor(int flavorId) throws IOException {
        synchronized(flavors) {
            for(Flavor f: flavors) {
                if(f.getId() == flavorId) {
                    return f;
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Flavor updateFlavor(int updatedId, String updatedName, String updatedDescription, Integer updatedQuantity) throws IOException {
        synchronized(flavors) {
            for(int i = 0; i < flavors.size(); ++i) {
                Flavor flavor = flavors.get(i);

                if(flavor.getId() != updatedId) {continue;}

                if(!(updatedName == null || updatedName.isEmpty() || flavor.getName().equals(updatedName))) {
                    flavor.setName(updatedName);
                } 

                if(!(updatedDescription == null || updatedDescription.isEmpty())) {
                    flavor.setDescription(updatedDescription);
                }

                if (updatedQuantity != null) {
                    flavor.setQuantity(updatedQuantity);
                }

                saveFlavors();
                return flavor;
            }
        }
        return null;
    }

    public Flavor[] searchFlavors(String name) {
        ArrayList<Flavor> foundFlavors = new ArrayList<>();
        for (Flavor flavor : flavors) {
            if(flavor.getName().toLowerCase().contains(name.toLowerCase())) {
                foundFlavors.add(flavor);
            }
        }
        Flavor[] arr = new Flavor[foundFlavors.size()];
        foundFlavors.toArray(arr);
        return arr;
    }

    /**
     * {@inheritDoc}
     */
    public boolean deleteFlavor(int id) throws IOException{
        synchronized(flavors) {
            for(Flavor f : flavors) {
                if(f.getId() == id) {
                    flavors.remove(f);
                    saveFlavors();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * load flavor array from file storage
     * @return true if no issues occurred
     * @throws IOException
     */
    private boolean loadFlavors() throws IOException{
        flavors = new ArrayList<Flavor>();
        nextId = 0;

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Flavor.class, new FlavorDeserializer());
        objectMapper.registerModule(module);
        
        Flavor[] flavorArray = objectMapper.readValue(new File(filename), Flavor[].class);

        for(Flavor f : flavorArray) {
            flavors.add(f);
            if(f.getId() > nextId) { //set nextId to current greatest ID
                nextId = f.getId(); 
            }
        }

        return true;
    }

    /**
     * saves flavors to file from current flavors list
     * @return true if nothing went wrong
     * @throws IOException
     */
    private boolean saveFlavors() throws IOException {
        Flavor[] flavorArray = getAllFlavors();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Flavor.class, new FlavorSerializer());
        objectMapper.registerModule(module);
        objectMapper.writeValue(new File(filename), flavorArray);
        return true;
    }

    /**
     * gets the next valid ID.
     * @return the next valid ID
     */
    private static int getNewId() {
        synchronized(nextId) {
            nextId++;
            return nextId;
        }

    }
    
}

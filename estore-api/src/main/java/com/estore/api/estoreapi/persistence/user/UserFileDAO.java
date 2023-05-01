package com.estore.api.estoreapi.persistence.user;

import com.estore.api.estoreapi.controller.PackController;
import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.aqua.AquaValidateData;
import com.estore.api.estoreapi.model.aqua.PackSize;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.model.user.User;
import com.estore.api.estoreapi.persistence.flavor.FlavorDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.apache.catalina.connector.OutputBuffer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
public class UserFileDAO implements UserDAO{
    private HashMap<String, User> users = new HashMap<>();
    private ObjectMapper objectMapper;
    private String filename;
    public User admin;

    public UserFileDAO(@Value("${users.file}") String filename, ObjectMapper objectMapper) throws IOException{
        this.filename = filename;
        this.objectMapper = objectMapper;
        loadUsers();
    }

    public User getUser(UUID uuid) {
        return users.get(uuid.toString());
    }

    public User getUser(String username) {
        for (User u : users.values()) {
            if(u.getUserName().equals(username)) return u;
        }
        return null;
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public boolean isAdmin(String uuid) {
        return uuid.equals(admin.getUUIDString());
    }

    //should not be used on frontend
    public boolean removeUser(String uuid) {
        User removedUser = users.remove(uuid);
        try {
            saveUsers();
        } catch(Exception e) {
            System.out.println(e);
        }
        return removedUser != null;
    }

    public User createUser(String userName) {
        User newUser = new User(userName);
        users.put(newUser.getUUIDString(), newUser);
        if(userName.equals("admin")) {
            admin = newUser;
        }
        try {
            saveUsers();
        } catch (IOException e) {
            System.out.println(e);
            throw new Error();
        }
        return newUser;
    }

    private boolean loadUsers() throws IOException {
        users = new HashMap<>();

        SimpleModule module = new SimpleModule();
        System.out.println(1);
        module.addDeserializer(User.class, new UserDeserializer());
        objectMapper.registerModule(module);
        System.out.println(2);
        File file = new File(filename);
        User[] userArray = objectMapper.readValue(file, User[].class);
        System.out.println(3);
        for(User u : userArray) {
            if(u.getUserName().equals("admin")) {
                admin = u;
            }
            users.put(u.getUUIDString(), u);
        }
        System.out.println(4);

        return true;
    }

    private boolean saveUsers() throws IOException {
        User[] userArray = users.values().toArray(new User[users.size()]);
        SimpleModule module = new SimpleModule();
        module.addSerializer(User.class, new UserSerializer());
        objectMapper.registerModule(module);
        objectMapper.writeValue(new File(filename), userArray);
        return true;
    }


    public String[] validateInventory(String uuid, Flavor[] flavors, AquaValidateData data) {
        User user = getUser(UUID.fromString(uuid));
        ArrayList<Aqua> inventory = (ArrayList<Aqua>)user.getCart().clone();
        inventory.addAll(user.getPacks());
        
        for(Aqua a : inventory) {
            System.out.println("a " + a);
        }

        ArrayList<String> output = new ArrayList<>();

        for(Aqua a : inventory) {
            output.addAll(validateAqua(a, flavors));
        }
        if(data != null) {
            for(Flavor f : data.flavors) {
                if(!validFlavor(f, flavors))
                output.add("create aqua | flavor " + f.getName() + " is not a valid flavor");
            }
            output.addAll(enoughInventory(inventory, flavors, data));
        } else {
            output.addAll(enoughInventory(inventory, flavors, null));
        }


        String[] arr = new String[output.size()];
        arr = output.toArray(arr);
        return arr;
    }

    public ArrayList<String> validateAqua(Aqua aqua, Flavor[] flavors) {
        ArrayList<String> output = new ArrayList<>();
        for(Flavor f : aqua.getFlavors()) {
            if(!validFlavor(f, flavors)) {
                output.add(aqua.getId() + "| flavor " + f.getName() + " is not a valid flavor");
            } 
        }   
        return output;
    }

    public boolean validFlavor(Flavor flavor, Flavor[] validFlavors) {
        for(Flavor f : validFlavors) {
            if(f.getName().equals(flavor.getName())) return true;
        }
        return false;
    }

    public ArrayList<String> enoughInventory(ArrayList<Aqua> aquas, Flavor[] validFlavors, AquaValidateData data) {
        HashMap<Integer, Integer> flavorCount = new HashMap<>();//id, quantity
        ArrayList<String> output = new ArrayList<>();

        for(Flavor flavor : validFlavors) {
            flavorCount.put(flavor.getId(), 0);
        }
        for(Aqua a : aquas) {
            for(Flavor f : a.getFlavors()) {
                Integer quantity = flavorCount.get(f.getId());
                if(quantity != null) {
                    flavorCount.put(f.getId(), quantity + a.getPackSize().getSize());
                    
                }
            }
        }

        if(data != null) {//this is coming from validate userinventoryandaqua
            for(Flavor f : data.flavors) {
                Integer quantity = flavorCount.get(f.getId());
                if(quantity != null) {
                    flavorCount.put(f.getId(), quantity + 1);
                    
                }
            }
        }
        
    

        for(var pair : flavorCount.entrySet()) {
            Flavor f = getFlavor(pair.getKey(), validFlavors);
            if(f.getQuantity() < pair.getValue()) {
                String str = "";
                for(Aqua a : getAquasWithFlavor(aquas, f)) {
                    str += a.getId() + ",";
                }
                if(str.length() > 0 && str.charAt(str.length()-1) == ',') {
                    str = str.substring(0, str.length()-1);
                }
                str += "|there is not enough stock for flavor " + f.getName();
                output.add(str);
                System.out.println(str);
            }
        }
        return output;
    }

    private Flavor getFlavor(int id, Flavor[] flavors) {
        for(Flavor f : flavors) {
            if(f.getId() == id) return f;
        }
        return null;
    }

    private ArrayList<Aqua> getAquasWithFlavor(ArrayList<Aqua> aquas, Flavor flavor) {
        ArrayList<Aqua> output = new ArrayList<>();
        for(Aqua a : aquas) {
            for(Flavor f : a.getFlavors()) {
                if(f.getId() ==  flavor.getId() && f.getName().equals(flavor.getName())) {
                    output.add(a);
                    break;
                }
            }
        }
        return output;
    }

    public ArrayList<Aqua> deletePack(String uuid, int packID) {
        User user = getUser(UUID.fromString(uuid));
        var pack = user.getPacks();
        ArrayList<Aqua> result = new ArrayList<>();
        for(int i = 0; i < pack.size(); i++) {
            if(pack.get(i).getOrderID() == packID) {
                result.add(pack.remove(i));
            }
        }
        return result;
    }

    public Aqua[] getPastOrders(String uuid) {
        User user = getUser(UUID.fromString(uuid));
        Aqua[] result = new Aqua[user.getPastOrders().size()];
        result = user.getPastOrders().toArray(result);
        return result;
    }

    @Override
    public void addToCart(String uuid, Aqua aqua) throws IOException {
        User u = getUser(UUID.fromString(uuid));
        try{
            u.addToCart(aqua);
            saveUsers();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public boolean removeFromCart(String uuid, int id) throws IOException {
        User u = getUser(UUID.fromString(uuid));
        try{
            boolean bool = u.removeFromCart(id);
            saveUsers();
            return bool;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    @Override
    public ArrayList<Aqua> getCart(String uuid) throws Exception {
        User u = getUser(UUID.fromString(uuid));
        try{
            return u.getCart();
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    //packID is the same as the order of each of the aquas in the pack
    public ArrayList<Aqua> getPack(String uuid, int packID) {
        User user = getUser(UUID.fromString(uuid));
        ArrayList<Aqua> pack = new ArrayList<>();
        for(Aqua a : user.getPacks()) {
            if(a.getOrderID() == packID) {
                pack.add(a);
            }
        }
        assert(pack.size() <= 6);
        return pack;
    }

    public ArrayList<Aqua> getAllPacks(String uuid) {
        User user = getUser(UUID.fromString(uuid));
        return user.getPacks();
    }

    //todo figure out how it will work if there are multiple packs not at 6
    //this could happen if the user exits or switches pages or if it is deleted i think
    
    //returns the pack without 6 aquas in it. 
    //if all have 6 returns the one with the highest
    //(actually lowest because neg) orderid
    public ArrayList<Aqua> getCurrentPack(String uuid) {
        User user = getUser(UUID.fromString(uuid));
       var allPacks = user.unflattenPack();
        if(allPacks.size() == 0) {
            return null;
        }

        ArrayList<Aqua> mostRecent = allPacks.get(0);//index out of bounds
        for(var pack : allPacks) {
            if(pack.size() != 6) {
                //when the user submits the pack it will add an aqua to the end of the pack with the notvalid type to tell that it is time for a new pack
                if(pack.size() == 1 && pack.get(0).getName().equals(PackController.packTerminater.getName())) {      
                    return null;
                }
                return pack;
            } else if(pack.get(0).getOrderID() < mostRecent.get(0).getOrderID()) {
                mostRecent = pack;
            }
        }

        return mostRecent;
    }

    public Aqua addToPack(String uuid, Aqua aqua) {
        var pack = getCurrentPack(uuid);
        if(pack == null || pack.size() == 6) {
            //remove terminator if you are adding a new aqua
            var packs = getUser(UUID.fromString(uuid)).getPacks();
            if(packs.size() != 0 && packs.get(packs.size()-1).getName().equals(PackController.packTerminater.getName())) {
                packs.remove(packs.size()-1);
            }
            //end comment
            
            aqua.setOrderID(nextOrderID(uuid));
        } else {
            aqua.setOrderID(pack.get(0).getOrderID());
        }

        aqua.setPackSize(PackSize.SINGLE);
        getUser(UUID.fromString(uuid)).addToPack(aqua);
        try {
            saveUsers();
        } catch (IOException e) {
            System.out.println(e);
        }
        return aqua;
    }

    private int nextOrderID(String uuid) {
        User user = getUser(UUID.fromString(uuid));
        var allPacks = user.getPacks();
        if(allPacks.size() == 0) return -1;

        int min = 1000;
        for(var a : allPacks) {
            if(a.getOrderID() < min) {
                min = a.getOrderID();
            }
        }
        return min - 1;
    }

    public Aqua editAquaInPack(String uuid, int aquaID, Aqua newAqua) {
        var user = getUser(UUID.fromString(uuid));
        var allPacks = user.getPacks();

        for(Aqua a : allPacks) {
            if(a.getId() == aquaID) {
                //pack size does should be single so you dont want to edit it
                a.setFlavors(newAqua.getFlavors());
                a.setName(newAqua.getName());
                return a;
            }
        }
        return null;
    }

    public Aqua removeFromPack(String uuid, int aquaID) {
        var user = getUser(UUID.fromString(uuid));
        var allPacks = user.getPacks();

        for(Aqua a : allPacks) {
            if(a.getId() == aquaID) {
               allPacks.remove(a);
               return a;
            }
        }
        return null;
    }

    @Override
    public Aqua[] checkout(String uuid) throws Exception {
        User u = getUser(UUID.fromString(uuid));
        try{
            
            Aqua[] arr = u.checkout();
            saveUsers();
            return arr;
        }
        catch (Exception e){
            throw new Error(e);
            //System.out.println("checkout exeption" + e);
            //return null;
        }
    }
}


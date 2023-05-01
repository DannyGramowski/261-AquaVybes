package com.estore.api.estoreapi.model.user;

import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.persistence.aqua.AquaDeserializer;
import com.estore.api.estoreapi.persistence.aqua.AquaSerializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.model.aqua.PackSize;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class User {
    @JsonProperty private String userName;
    @JsonProperty private UUID uuid;
    @JsonProperty private ArrayList<Aqua> cart;
    @JsonProperty private ArrayList<Aqua> pastOrders;
    @JsonProperty private ArrayList<Aqua> packs;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public User(@JsonProperty("userName") String userName) {
        this(userName, UUID.randomUUID().toString());
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public User(@JsonProperty("userName") String userName, @JsonProperty("uuid") String uuid) {
        this.userName = userName;
        this.uuid = UUID.fromString(uuid);
        this.cart = new ArrayList<Aqua>();
        this.pastOrders = new ArrayList<>();
        this.packs = new ArrayList<>();
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public User(@JsonProperty("userName") String userName,
                @JsonProperty("uuid") String uuid,
                @JsonProperty("cart") ArrayList<Aqua> cart,
                @JsonProperty("pastOrders") ArrayList<Aqua> previousOrders,
                @JsonProperty("packs") ArrayList<Aqua> packs) {
        this.userName = userName;
        this.uuid = UUID.fromString(uuid);
        this.cart = cart;
        this.pastOrders = previousOrders;
        this.packs = packs;
    }

    @JsonProperty("userName")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("uuid")
    public String getUUIDString() {
        return uuid.toString();
    }

    public UUID getUUID() {return uuid;}
    @Override
    public String toString() {
        return "User{" + "userName='" + userName + '\'' + ", uuid=" + uuid + '}';
    }

    @JsonProperty("cart")
    public ArrayList<Aqua> getCart(){
        return cart;
    }

    @JsonProperty("pastOrders")
    public ArrayList<Aqua> getPastOrders() {
        return pastOrders;
    }

    @JsonProperty("packs")
    public ArrayList<Aqua> getPacks() {
        return packs;
    }
    
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public void setPastOrders(ArrayList<Aqua> pastOrders) {
        this.pastOrders = pastOrders;
    }

    public void addToCart(Aqua aqua){
        aqua.setOrderID(getCurrentOrderID());
        cart.add(aqua);
    }

    public void addToPack(Aqua aqua) {
        packs.add(aqua);
    }

    public boolean removeFromCart(int id){
        for(int i = 0; i < cart.size(); i++) {
            if(cart.get(i).getId() == id) {
                cart.remove(i);
                return true;
            }
        }
        return false;
    }

    public Aqua[] checkout(){
        var packs = unflattenPack();
        var flattenedPack = getPacks();
        ArrayList<Aqua> checkedOut = new ArrayList<>();

        for(ArrayList<Aqua> arr : packs) {
            if(arr.size() == 6) {
                for(Aqua a : arr) {
                    flattenedPack.remove(a);
                    checkedOut.add(a);
                    pastOrders.add(a);
                }
            }
        }

        for(Aqua a : cart) {
            checkedOut.add(a);
            pastOrders.add(a);
        }

        Aqua[] arr = new Aqua[checkedOut.size()];
        arr = checkedOut.toArray(arr);
        
        for(Aqua a : arr) {
            
        }
            //todo work order is not sent anywhere
        generateWorkOrder(checkedOut);

        cart.clear();
        // work order functions here
        return arr;
    }

    public ArrayList<ArrayList<Aqua>> unflattenPack() {
        ArrayList<ArrayList<Aqua>> allPacks = new ArrayList<>(); //yes this is weird ik
        if(getPacks() == null) {
            return null;
        }
        for(Aqua a : getPacks()) {
            boolean added = false;
            for(ArrayList<Aqua> list : allPacks) {
                if(list.size() > 0 && list.get(0).getOrderID() == a.getOrderID()) {
                    list.add(a);
                    added = true;
                }
            }

            if(!added) {
                var newArr = new ArrayList<Aqua>();
                newArr.add(a);
                allPacks.add(newArr);
            }
        }

        return allPacks;
    }

    public int getCurrentOrderID() {
        int biggestID = -1;
        if(cart.size() == 0) {
            for(Aqua a : getPastOrders()) {
                if(a.getOrderID() > biggestID) {
                    biggestID = a.getOrderID();
                }
            }
            return ++biggestID;
        } else {
            return cart.get(0).getOrderID();
        }
    }

    /**
     * Creates a work order for the workers at the shipping facility from an array list of Aquas,
     * in the form of a JSON.
     * @param order
     * @return the string 
     * @throws IOException
     */
    public String generateWorkOrder(ArrayList<Aqua> order) {
        StringBuilder workOrder = new StringBuilder();
        int packNum = 1;
        for (Aqua a : order) {
            workOrder.append("{Pack #"+packNum+": Pack of "+a.getPackSize()+" | Flavor(s): "+a.getFlavorsStrings()
            +" | ID NUM: "+Integer.toString(a.getId())+ " | orderID " + a.getOrderID() + "}");
            packNum++;
        }
        
        return  workOrder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userName.equals(user.userName) && uuid.equals(user.uuid);
    }

    public Object thenReturn(User user) {
        return null;
    }
}

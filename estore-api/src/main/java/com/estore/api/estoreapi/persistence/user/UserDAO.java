package com.estore.api.estoreapi.persistence.user;

import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.aqua.AquaValidateData;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.model.user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public interface UserDAO {

    public User getUser(UUID uuid);
    public User getUser(String userName);
    public User createUser(String userName);
    public boolean isAdmin(String uuid);
    public boolean removeUser(String uuid);
    /**
     * adds Aqua to cart
     * @param uuid user id for cart
     * @param aqua Aqua to be added
     * @throws IOException
     */
    public void addToCart(String uuid, Aqua aqua) throws Exception;

    /**
     * removes Aqua from cart
     * @param uuid user id for cart
     * @param aqua Aqua to be removed
     * @return true if found; false otherwise
     * @throws IOException
     */
    public boolean removeFromCart(String uuid, int id) throws IOException;

    /**
     * returns full list of Aquas
     * @param uuid user id for cart
     * @return ArrayList<Aqua> representing cart
     * @throws IOException
     * @throws Exception
     */
    public ArrayList<Aqua> getCart(String uuid) throws Exception;

    public Aqua[] getPastOrders(String uuid);


    public ArrayList<String> enoughInventory(ArrayList<Aqua> aquas, Flavor[] validFlavors, AquaValidateData data) ;
    public boolean validFlavor(Flavor flavor, Flavor[] validFlavors) ;
    public ArrayList<String> validateAqua(Aqua aqua, Flavor[] flavors) ;
    public String[] validateInventory(String uuid, Flavor[] flavors, AquaValidateData data);


    public ArrayList<Aqua> getPack(String uuid, int packID); 
    public ArrayList<Aqua> getCurrentPack(String uuid);
    public Aqua addToPack(String uuid, Aqua aqua);
    public Aqua editAquaInPack(String uuid, int aquaID, Aqua newAqua);
    public Aqua removeFromPack(String uuid, int aquaID);
    public ArrayList<Aqua> getAllPacks(String uuid); 
    public ArrayList<Aqua> deletePack(String uuid, int packID);

    /**
     * checks out cart by removing all items and returning
     * a list of all Aquas in the cart
     * @return Aqua[] containing all Aquas at time of checkout
     */
    public Aqua[] checkout(String uuid) throws Exception;
}

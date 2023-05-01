package com.estore.api.estoreapi.persistence.flavor;

import java.io.IOException;
import java.util.ArrayList;

import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.flavor.Flavor;

public interface FlavorDAO {

    /**
     * creates a new flavor with an id that is generated and name that is provided
     * @param flavorName name of new flavor
     * @param flavorDescription description of new flavor
     * @param flavorQuantity The quantity of the flavor in inventory
     * @return new flavor
     * @throws IOException
     */
    public Flavor createFlavor(String flavorName, String flavorDescription, int flavorQuantity) throws IOException;

    public Flavor[] searchFlavors(String name);
    public void removeFromInventory(ArrayList<Aqua> aquas);
    
    

    /**
     * deletes flavor with matching id
     * @param flavorId id of flavor to delete
     * @return true if successful deletion, false otherwise
     * @throws IOException
     */
    public boolean deleteFlavor(int flavorId) throws IOException;

    /**
     * gets all flavors
     * @return list of flavors
     * @throws IOException
     */
    public Flavor[] getAllFlavors() throws IOException;

    /**
     * get a flavor by a particular id
     * @param flavorId the id of the flavor you wish to get
     * @return flavor with matching id as flavorId
     * @throws IOException
     */
    public Flavor getFlavor(int flavorId) throws IOException;

    /**
     * updates a flavor's information
     * @param updatedId new id
     * @param updatedName new name
     * @param updatedDescription new description
     * @param updatedQuantity new quantity
     * @return flavor with updated information
     * @throws IOException
     */
    public Flavor updateFlavor(int updatedId, String updatedName, String updatedDescription, Integer updatedQuantity) throws IOException;
}

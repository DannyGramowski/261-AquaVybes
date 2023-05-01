package com.estore.api.estoreapi.persistence.aqua;

import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.model.aqua.PackSize;

import java.io.IOException;
import java.util.ArrayList;

public interface AquaDAO {
    /**
     * When called, we have passed in every value that is going to end up in the product itself
     * and the Aqua object is actally constructed. Additionally, if no name is provided, then this
     * creates the default name for the product in the form of "FLAVOR-FLAVOR-###Pack"
     * @param name The name given to the product, or DEFAULT where we calculate the namee
     * @param description Optional chance to add information about the the product, usually empty
     * @param flavors The list of the one or multiple flavors in the drink, at least one flavor is assumed.
     * @param packSize The number of units in that product, of a preapproved list
     */
    public Aqua createAqua(String name, ArrayList<Flavor> flavors, PackSize packSize, int orderID) throws IOException;

    /**
     * gets the aqua with the given id
     * @param id the id of the aqua you want to find
     * @return the aqua with the given id
     */
    public Aqua getAqua(int id) throws IOException;

    /**
     * gets the aqua with the given name
     * @param name the name of the aqua you want to find
     * @return the aqua with the given name
     */
    public Aqua getAqua(String name) throws IOException;

    /**
     * gets the inventory of aqua
     * @return inventory
     * @throws IOException if issue with file storage
     */
    public Aqua[] getInventory() throws IOException;

    /**
     * deletes the aqua with the given id
     * @param id the id of the aqua you want to delete
     * @return true: deletion successful; false: otherwise
     */
    public boolean deleteAqua(int id) throws IOException;

    /**
     * updates an aqua with the provided arguments
     * @param id the id of the aqua to update
     * @param name The name of the aqua
     * @param description a description of the aqua
     * @param flavors a list of the aqua's flavors
     * @param packSize the size of the pack
     */
    public Aqua updateAqua(int id, String name, ArrayList<Flavor> flavors, PackSize packSize) throws IOException;

    /**
     * searches the inventory for any aquas 
     * containing the provided query
     * @param query the query to search for
     * @return array of matching results
     */
    public Aqua[] searchInventory(String query) throws IOException;

    /**
     * searches the inventory for any aquas 
     * made of the provided Flavor
     * @param flavor the flavor to search for
     * @return array of matching results
     */
    public Aqua[] searchInventory(Flavor flavor) throws IOException;
}

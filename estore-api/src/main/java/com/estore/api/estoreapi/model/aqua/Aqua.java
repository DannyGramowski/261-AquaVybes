package com.estore.api.estoreapi.model.aqua;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.persistence.flavor.FlavorDAO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Aqua {
    private static Integer maxId = -1;
    
    private final int id;
    private String name;
    private ArrayList<Flavor> flavors = new ArrayList<>();
    private PackSize packSize;
    private int orderID;

    /**
     * creates a new Aqua object
     * @param id id of the aqua
     * @param name name of the aqua
     * @param flavors flavors of the aqua
     * @param packSize pack size of the aqua
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Aqua(@JsonProperty("id") int id, @JsonProperty("name") String name,
                @JsonProperty("flavors") ArrayList<Flavor> flavors,
                @JsonProperty("packSize") PackSize packSize,
                @JsonProperty("orderID") int orderID) {
        this.id = id;
        this.name = name;                   //generated from pack-size and flavors, Customizable
        this.flavors = flavors;             //array of enums
        this.packSize = packSize;           //Enum.
        this.orderID = orderID;

        synchronized(maxId) {
            maxId = id > maxId ? id : maxId;
        }
    }

    /**
     * creates a new Aqua object
     * @param name name of the aqua
     * @param flavors flavors of the aqua
     * @param packSize pack size of the aqua
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Aqua(@JsonProperty("name") String name,
                @JsonProperty("flavors") ArrayList<Flavor> flavors,
                @JsonProperty("packSize") PackSize packSize,
                @JsonProperty("orderID") int orderID) {
        this.name = name;                   //generated from pack-size and flavors, Customizable
        this.flavors = flavors;             //array of enums
        this.packSize = packSize;           //Enum.
        this.orderID = orderID;

        synchronized(maxId) {
            this.id = maxId++;
        }
    }

    /**
     * Copies another Aqua object with a new id
     * @param other Another aqua
     */
    public Aqua(Aqua other) {
        this.name = other.getName();
        this.flavors = other.getFlavors();
        this.packSize = other.getPackSize();
        this.orderID = other.getOrderID();
        synchronized(maxId) {
            this.id = maxId++;
        }
    }

    public Aqua(ArrayList<Flavor> flavors) {
        synchronized(maxId) {
            this.id = maxId++;
        }

        this.flavors = flavors;
        this.name = "";
        this.packSize = PackSize.SINGLE;
        this.orderID = -1;
    }

    /**
     * Generate a name for the aqua with the given flavors and pack size.
     * @param flavors the flavors of the current aqua
     * @param packSize the number of waters in the pack
     * @return a string describing an aqua with the given flavors and size
     */
    public static StringBuilder generateName(ArrayList<Flavor> flavors, PackSize packSize) {
        StringBuilder newName = new StringBuilder("");
        for (Flavor f : flavors) {
            newName.append(f.getName() +"-");
        }
        newName.append(packSize.name() + "-PACK");
        return newName;
    }

    public String getAquaCode() {
        return "{\"code\":\"" +
                String.join("-",
                        flavors.stream()
                               .map((Flavor flavor) -> String.format("%x", flavor.getId()))
                               .toArray(String[]::new)) +
                "\"}";
    }

    /**
     * calculates the expected price
     * @return expected price
     */
    private double priceCalculator() {
        //Give formula using attributes
        return packSize.getSize() * .5;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }


    @JsonProperty("flavors")
    public ArrayList<Flavor> getFlavors() {
        return flavors;
    }

    @JsonProperty("orderID")
    public int getOrderID() {
        return orderID;
    }

    public String getFlavorsStrings() {
        StringBuilder flavorList = new StringBuilder();
        for (Flavor f : flavors) {
            flavorList.append(f.getName()+", ");
        }
        flavorList.delete(flavorList.length()-2, flavorList.length());
        return flavorList.toString();
    }


    @JsonProperty("packSize")
    public PackSize getPackSize() {
        return packSize;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFlavors(ArrayList<Flavor> flavors) {
        this.flavors = flavors;
    }

    public void setPackSize(PackSize packSize) {
        this.packSize = packSize;
    }

    public void setOrderID(int id) {
        this.orderID = id;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aqua aqua = (Aqua) o;
        
        return id == aqua.id && packSize == aqua.packSize && name.equals(aqua.name)
            && flavors.equals(aqua.flavors) && aqua.orderID == orderID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, packSize);
        result = 31 * result + flavors.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Aqua{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", flavors=" + flavors +
                ", packSize=" + packSize +
                ", orderID=" + orderID +
                '}';
    }
}

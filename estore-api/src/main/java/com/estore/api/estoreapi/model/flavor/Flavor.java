package com.estore.api.estoreapi.model.flavor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Flavor {
    @JsonProperty private int id;
    @JsonProperty private String name;
    @JsonProperty private String description;
    @JsonProperty private int quantity;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Flavor(@JsonProperty("id") int id,
                @JsonProperty("name") String name,
                @JsonProperty("description") String description,
                @JsonProperty("quantity") int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("quantity")
    public int getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Flavor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + description + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Flavor flavor = (Flavor) obj;
        return name.equals(flavor.name)
                && id == flavor.getId()
                && description == flavor.getDescription()
                && quantity == flavor.getQuantity();
    }

}
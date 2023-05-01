package com.estore.api.estoreapi.persistence.aqua;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.model.aqua.PackSize;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

/**
 * CUSTOM DESERIALIZER
 */
public class AquaDeserializer extends StdDeserializer<Aqua> {
    public AquaDeserializer() {
        this(null);
    }


    public AquaDeserializer(Class<?> vc) {
        super(vc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Aqua deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        JsonNode node = parser.getCodec().readTree(parser);
        int id = (Integer) ((IntNode) node.get("id")).numberValue();
        String name = node.get("name").asText("DEFAULT");
        //String desc = node.get("description").asText("default description");
        Iterator<JsonNode> arrayNode = node.get("flavors").elements();
        //Double price = node.get("price").asDouble();
        
        String packSize = node.get("packSize").asText("ONE");   
        //int quantity = node.get("quantity").asInt(1);

        return new Aqua(id, name, parseFlavors(arrayNode), PackSize.deserialize(packSize), 0);
    }

    /**
     * converts the json to an ArrayList of flavors
     * @param json json array
     * @return Arraylist of flavors from json iterator
     */
    private ArrayList<Flavor> parseFlavors(Iterator<JsonNode> json) {
        ArrayList<Flavor> flavors = new ArrayList<>();
        while(json.hasNext()) {
            JsonNode currentNode = json.next();
            int flavorId = currentNode.get("id").asInt();
            String flavorName = currentNode.get("name").asText();
            String flavorDesc = currentNode.get("description").asText();
            Flavor flavor = new Flavor(flavorId, flavorName, flavorDesc, 1);
            flavors.add(flavor);
        }
        return flavors;
    }
    
}

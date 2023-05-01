package com.estore.api.estoreapi.persistence.user;

import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.aqua.PackSize;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.model.user.User;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class UserDeserializer extends StdDeserializer {
    public UserDeserializer() {this(null);}
    public UserDeserializer(Class<?> vc) {super(vc);}

    /**
     * {@inheritDoc}
     */
    @Override
    public User deserialize(JsonParser parser, DeserializationContext ctxt) {
        JsonNode node = null;
        try {
            node = parser.getCodec().readTree(parser);
        } catch(IOException e) {
            System.out.println(e);
            //throw new {public class NUllError extends E};
        }
        String userName = node.get("userName").asText();
        String uuid = node.get("uuid").asText();
        ArrayList<Aqua> cart = parseAquas(node.get("cart").elements());
        ArrayList<Aqua> previousOrders = parseAquas(node.get("pastOrders").elements());
        ArrayList<Aqua> packs = parseAquas(node.get("packs").elements());
        return new User(userName, uuid, cart, previousOrders, packs);
    }

    private ArrayList<Aqua> parseAquas(Iterator<JsonNode> json) {
        ArrayList<Aqua> aquas = new ArrayList<>();

        while(json.hasNext()) {
            JsonNode currentNode = json.next();
            aquas.add(deserializeAqua(currentNode));
        }

        return aquas;
    }

    public Aqua deserializeAqua(JsonNode node) {      
        int id = (Integer) ((IntNode) node.get("id")).numberValue();
        String name = node.get("name").asText("DEFAULT");
        //String desc = node.get("description").asText("default description");
        Iterator<JsonNode> arrayNode = node.get("flavors").elements();
        //Double price = node.get("price").asDouble();
        
        String packSize = node.get("packSize").asText("ONE");
        int orderID = node.get("orderID").asInt();   
        //int quantity = node.get("quantity").asInt(1);

        return new Aqua(id, name, parseFlavors(arrayNode), PackSize.deserialize(packSize), orderID);
    }

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

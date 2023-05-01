package com.estore.api.estoreapi.persistence.flavor;

import java.io.IOException;

import com.estore.api.estoreapi.model.flavor.Flavor;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class FlavorDeserializer extends StdDeserializer<Flavor> {
    public FlavorDeserializer() {
        this(null);
    }

    public FlavorDeserializer(Class<?> vc) {
        super(vc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flavor deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = parser.getCodec().readTree(parser);
        int flavorId = node.get("id").asInt();
        String flavorName = node.get("name").asText();
        String flavorDescription = node.get("description").asText();
        int flavorQuantity = node.get("quantity").asInt();
        return new Flavor(flavorId, flavorName, flavorDescription, flavorQuantity);
    }
}

package com.estore.api.estoreapi.persistence.aqua;

import java.io.IOException;

import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * CUSTOM SERIALIZER
 */
public class AquaSerializer extends StdSerializer<Aqua> {
    public AquaSerializer() {
        this(null);
    }

    public AquaSerializer(Class<Aqua> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(Aqua a, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", a.getId());
        jsonGenerator.writeStringField("name", a.getName());
        jsonGenerator.writeArrayFieldStart("flavors");
        Flavor [] flavorArray = new Flavor[a.getFlavors().size()];
        a.getFlavors().toArray(flavorArray);
        for(Flavor f : flavorArray) {
            jsonGenerator.writePOJO(f);   
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeObjectField("packSize", a.getPackSize());
        jsonGenerator.writeNumberField("orderID", a.getOrderID());
        jsonGenerator.writeEndObject();
    }
    
}

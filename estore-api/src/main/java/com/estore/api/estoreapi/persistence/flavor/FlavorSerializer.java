package com.estore.api.estoreapi.persistence.flavor;

import java.io.IOException;

import com.estore.api.estoreapi.model.flavor.Flavor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class FlavorSerializer extends StdSerializer<Flavor> {
    public FlavorSerializer() {
        this(null);
    }

    public FlavorSerializer(Class<Flavor> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(Flavor value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", value.getId());
        jsonGenerator.writeStringField("name", value.getName());
        jsonGenerator.writeStringField("description", value.getDescription());
        jsonGenerator.writeEndObject();
    }
    
}

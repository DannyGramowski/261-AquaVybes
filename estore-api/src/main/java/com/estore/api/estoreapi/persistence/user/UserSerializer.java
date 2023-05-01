package com.estore.api.estoreapi.persistence.user;

import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.user.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class UserSerializer extends StdSerializer<User> {
    public UserSerializer() {
        this(null);
    }
    public UserSerializer(Class<User> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(User value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("userName", value.getUserName());
        jsonGenerator.writeStringField("uuid", value.getUUIDString());

        jsonGenerator.writeArrayFieldStart("cart");
        Aqua[] aquaArray = new Aqua[value.getCart().size()];
        value.getCart().toArray(aquaArray);
        for(Aqua a: aquaArray) {
            jsonGenerator.writePOJO(a);
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeArrayFieldStart("pastOrders");
        Aqua[] orderArray = new Aqua[value.getPastOrders().size()];
        value.getPastOrders().toArray(orderArray);
        for(Aqua a: orderArray) {
            jsonGenerator.writePOJO(a);
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeArrayFieldStart("packs");
        System.out.println("packs " + value.getPacks());
        if(value.getPacks() == null) {
            throw new Error("fuckkk");
        }
        Aqua[] packArray = new Aqua[value.getPacks().size()];
        value.getPacks().toArray(packArray);
        System.out.println("pack arr " + packArray);
        for(Aqua a: packArray) {
            jsonGenerator.writePOJO(a);
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }
}

package com.microservices.authorizationserver.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.microservices.authorizationserver.modal.Authority;
import com.microservices.authorizationserver.modal.Privilege;

import java.io.IOException;

public class RoleSerializer extends StdSerializer<Authority> {

    public RoleSerializer() {
        this(null);
    }

    public RoleSerializer(Class<Authority> t) {
        super(t);
    }

    @Override
    public void serialize(Authority value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("@class", Authority.class.getCanonicalName());
        gen.writeStringField("id", String.valueOf(value.getId()));
        gen.writeStringField("desc", String.valueOf(value.getDescription()));
        gen.writeArrayFieldStart("privileges");
        for (Privilege privilege : value.getPrivileges()) {
            gen.writeStartObject();
            gen.writeStringField("@class", Privilege.class.getCanonicalName());
            gen.writeStringField("id", String.valueOf(privilege.getId()));
            gen.writeStringField("desc", String.valueOf(privilege.getDescription()));
            gen.writeEndObject();
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(Authority value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        serialize(value, gen, serializers);
    }
}

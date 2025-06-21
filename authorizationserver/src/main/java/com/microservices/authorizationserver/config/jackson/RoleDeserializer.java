package com.microservices.authorizationserver.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.microservices.authorizationserver.modal.Authority;
import com.microservices.authorizationserver.modal.Privilege;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RoleDeserializer extends StdDeserializer<Authority> {

    public RoleDeserializer() {
        this(null);
    }

    public RoleDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Authority deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        String id = node.get("id").asText();
        String description = node.get("desc").asText();

        Set<Privilege> privileges = new HashSet<>();
        JsonNode privilegesNode = node.get("privileges");
        if (privilegesNode.isArray()) {
            for (JsonNode privilegeNode : privilegesNode) {
                String privilegeId = privilegeNode.get("id").asText();
                String privilegeDesc = privilegeNode.get("desc").asText();
                Privilege privilege = new Privilege();
                privilege.setId(privilegeId);
                privilege.setDescription(privilegeDesc);
                privileges.add(privilege);
            }
        }

        // Create and return Role object
        Authority authority = new Authority();
        authority.setId(id);
        authority.setDescription(description);
        authority.setPrivileges(privileges);

        return authority;
    }
}

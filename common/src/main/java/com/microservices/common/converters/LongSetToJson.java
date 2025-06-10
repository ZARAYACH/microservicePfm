package com.microservices.common.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Set;

@Converter
public class LongSetToJson implements AttributeConverter<Set<Long>, String> {

    private final ObjectMapper objectMapper = new JsonMapper();

    @Override
    public String convertToDatabaseColumn(Set<Long> longs) {
        if (longs != null && !longs.isEmpty()) {
            try {
                return objectMapper.writeValueAsString(longs);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Couldn't convert Long Set to json", e);
            }
        }
        return "[]";
    }

    @Override
    public Set<Long> convertToEntityAttribute(String s) {
        if (StringUtils.isNotBlank(s)) {
            try {
                return objectMapper.readValue(s, new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Couldn't convert Json to Long Set", e);
            }
        }
        return Collections.emptySet();
    }
}

package com.innogrid.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

public final class JsonUtil {

    @Getter
    static final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    public static Object parse(String json, Class<?> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to convert String to %s", clazz.getName()), e);
        }
    }

    public static Object parse(String json, TypeReference<?> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("failed to convert String to " + typeReference.getType(), e);
        }
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }

        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to convert %s to String", obj.getClass().getName()), e);
        }
    }

}

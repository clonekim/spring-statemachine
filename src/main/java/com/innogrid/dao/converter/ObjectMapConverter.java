package com.innogrid.dao.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.innogrid.util.JsonUtil;
import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;
import org.jooq.JSONB;

import java.util.Map;

public class ObjectMapConverter implements Converter<JSONB, Map<Object, Object>> {

    @Override
    public Map<Object, Object> from(JSONB databaseObject) {

        if(databaseObject == null || databaseObject.data() == null)
            return Map.of();

        return (Map<Object, Object>) JsonUtil.parse(databaseObject.data(), new TypeReference<Map<Object, Object>>() {});
    }

    @Override
    public JSONB to(Map<Object, Object> userObject) {
        return JSONB.jsonb(JsonUtil.toJson(userObject));
    }

    @Override
    public @NotNull Class<JSONB> fromType() {
        return JSONB.class;
    }

    @Override
    public @NotNull Class<Map<Object, Object>> toType() {
        return (Class<Map<Object, Object>>) (Class<?>) Map.class;
    }
}

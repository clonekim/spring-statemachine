package com.innogrid.dao.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.innogrid.util.JsonUtil;
import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;
import org.jooq.JSONB;

import java.util.Map;

public class MapConverter implements Converter<JSONB, Map<String, Object>> {

    @Override
    public Map<String, Object> from(JSONB databaseObject) {

        if(databaseObject == null || databaseObject.data() == null)
            return Map.of();

        return (Map<String, Object>) JsonUtil.parse(databaseObject.data(), new TypeReference<Map<String, Object>>() {});
    }

    @Override
    public JSONB to(Map<String, Object> userObject) {
        return JSONB.jsonb(JsonUtil.toJson(userObject));
    }

    @Override
    public @NotNull Class<JSONB> fromType() {
        return JSONB.class;
    }

    @Override
    public @NotNull Class<Map<String, Object>> toType() {
        return (Class<Map<String, Object>>) (Class<?>) Map.class;
    }
}

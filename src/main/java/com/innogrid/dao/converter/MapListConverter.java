package com.innogrid.dao.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.innogrid.util.JsonUtil;
import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;
import org.jooq.JSONB;

import java.util.List;
import java.util.Map;

public class MapListConverter implements Converter<JSONB, List<Map<String, Object>>> {

    @Override
    public List<Map<String, Object>> from(JSONB databaseObject) {

        if(databaseObject == null || databaseObject.data() == null)
            return List.of();

        return (List<Map<String, Object>>) JsonUtil.parse(databaseObject.data(), new TypeReference<List<Map<String, Object>>>() {});
    }

    @Override
    public JSONB to(List<Map<String, Object>> userObject) {
        return JSONB.jsonb(JsonUtil.toJson(userObject));
    }

    @Override
    public @NotNull Class<JSONB> fromType() {
        return JSONB.class;
    }

    @Override
    public @NotNull Class<List<Map<String, Object>>> toType() {
        return (Class<List<Map<String, Object>>>) (Class<?>) List.class;
    }
}

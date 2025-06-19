package com.innogrid.dao;

import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Result;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface JooQDao<T, ID> {

    Record save(T entity);

    int insert(T entity);

    int[] batchInsert(List<T> entities);

    int update(T entity);

    int update(T entity, Condition... conditions);

    int update(Map<String, Object> map, Condition... conditions);

    int delete(ID id);

    int delete(Condition... conditions);

    Result<?> fetch(Condition... conditions);

    Optional<T> fetchOne(ID id);

    Optional<T> fetchOne(Condition... conditions);

    boolean exists(ID id);

    boolean exists(Condition... conditions);
}

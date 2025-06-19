package com.innogrid.dao;

import lombok.Getter;
import org.jooq.Record;
import org.jooq.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractJooQDao<T extends TableRecord<?>, ID> implements JooQDao<T,ID> {

    @Getter
    protected DSLContext create;
    protected Table<?> table;
    protected TableField<?, ID> idField;
    private final Class<T> clazz;

    @SuppressWarnings({"unchecked"})
    public AbstractJooQDao(DSLContext create, Table<?> table, TableField<?, ID> idField) {
        this.create = create;
        this.table = table;
        this.clazz = (Class<T>) table.getRecordType();
        this.idField = idField;
    }

    public Result<?> fetch(Condition... conditions) {
        return create.selectFrom(table).where(conditions).fetch();
    }

    @Override
    public Record save(T entity) {
        return Objects.requireNonNull(create.insertInto(entity.getTable())
                .set(entity)
                .returning()
                .fetchOne()).into(this.clazz);
    }

    @Override
    public int insert(T entity) {
        return create.insertInto(entity.getTable()).set(entity).execute();
    }

    @Override
    public int[] batchInsert(List<T> entities) {
       return create.batchInsert(entities).execute();
    }

    @Override
    public int update(T entity) {
        return create.executeUpdate((UpdatableRecord<?>) entity);
    }

    @Override
    public int update(T entity, Condition... conditions) {

        return create.update(table)
                .set(entity)
                .where(conditions)
                .execute();
    }


    @Override
    public int update(Map<String, Object> map, Condition... conditions) {
        return create.update(table)
                .set(map)
                .where(conditions)
                .execute();
    }


    @Override
    public int delete(ID id) {
        return create.delete(table).where(idField.eq(id))
                .execute();
    }


    @Override
    public int delete(Condition... conditions) {
        return create.delete(table).where(conditions).execute();
    }



    @Override
    public Optional<T> fetchOne(Condition... conditions) {
        return create.selectFrom(table)
                .where(conditions)
                .limit(1)
                .fetchOptionalInto(this.clazz);
    }


    @Override
    public Optional<T> fetchOne(ID id) {
        return create.selectFrom(table)
                .where(idField.eq(id))
                .fetchOptionalInto(clazz);
    }

    protected abstract ID getId(T record);


    @Override
    public boolean exists(Condition... conditions) {
        return create.fetchExists(table, conditions);
    }


    @Override
    public boolean exists(ID id) {
        return create.fetchExists(table, idField.eq(id));
    }
}

package com.innogrid.dao;

import com.innogrid.tables.records.DocumentRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.innogrid.Tables.DOCUMENT;

@Repository
public class DocumentDao {

    final DSLContext create;

    public DocumentDao(DSLContext create) {
        this.create = create;
    }

    public Optional<DocumentRecord> get(Integer id) {
        return create
                .select()
                .from(DOCUMENT)
                .where(DOCUMENT.ID.eq(id))
                .fetchOptionalInto(DocumentRecord.class);
    }

    public List<DocumentRecord> findAll() {
        return create
                .select()
                .from(DOCUMENT)
                .fetch().into(DocumentRecord.class);
    }

    public DocumentRecord insert(DocumentRecord record) {
        return Objects.requireNonNull(create
                .insertInto(DOCUMENT)
                .set(record)
                .returning()
                .fetchOne()).into(DocumentRecord.class);
    }

}

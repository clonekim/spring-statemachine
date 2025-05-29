package com.innogrid.dao;

import com.innogrid.tables.records.DocumentStateRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

import static com.innogrid.Tables.DOCUMENT_STATE;

@Repository
public class DocumentStateDao {

    final DSLContext create;

    public DocumentStateDao(DSLContext create) {
        this.create = create;
    }


    public DocumentStateRecord insert(DocumentStateRecord record) {
        return Objects.requireNonNull(create
                .insertInto(DOCUMENT_STATE)
                .set(record)
                .returning()
                .fetchOne()).into(DocumentStateRecord.class);
    }


    public Optional<DocumentStateRecord> getByDocId(Integer id) {
        return create
                .select()
                .from(DOCUMENT_STATE)
                .where(DOCUMENT_STATE.DOC_ID.eq(id))
                .fetchOptionalInto(DocumentStateRecord.class);
    }
}

package com.innogrid.dao;

import com.innogrid.tables.records.TicketStateRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.innogrid.Tables.TICKET_STATE;

@Repository
public class TicketStateDao {

    final DSLContext create;

    public TicketStateDao(DSLContext create) {
        this.create = create;
    }

    public int insert(TicketStateRecord record) {
        return create
                .insertInto(TICKET_STATE)
                .set(record)
                .execute();
    }

    public Optional<TicketStateRecord> findMachine(Integer ticketId) {
        return create
                .select()
                .from(TICKET_STATE)
                .where(TICKET_STATE.TICKET_ID.eq(ticketId))
                .fetchOptionalInto(TicketStateRecord.class);
    }
}

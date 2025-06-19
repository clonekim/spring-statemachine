package com.innogrid.dao;

import com.innogrid.tables.records.TicketRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.innogrid.Tables.TICKET;

@Repository
public class TicketDao extends AbstractJooQDao<TicketRecord, Integer> {

    public TicketDao(@Autowired DSLContext create) {
        super(create, TICKET, TICKET.ID);
    }


    @Override
    protected Integer getId(TicketRecord record) {
        return record.getId();
    }
}

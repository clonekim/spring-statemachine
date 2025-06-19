package com.innogrid.dao;

import com.innogrid.tables.records.TicketReviewerRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.innogrid.Tables.TICKET_REVIEWER;

@Repository
public class TicketReviewerDao extends AbstractJooQDao<TicketReviewerRecord, Integer> {


    public TicketReviewerDao(@Autowired DSLContext create) {
        super(create, TICKET_REVIEWER, TICKET_REVIEWER.ID);
    }

    @Override
    protected Integer getId(TicketReviewerRecord record) {
        return record.getId();
    }
}

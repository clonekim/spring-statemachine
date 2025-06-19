package com.innogrid.dao;

import com.innogrid.tables.records.StateMachineRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.innogrid.Tables.STATE_MACHINE;

@Repository
public class StateMachineDao extends AbstractJooQDao<StateMachineRecord, String> {

    public StateMachineDao(@Autowired DSLContext create) {
        super(create, STATE_MACHINE, STATE_MACHINE.MACHINE_ID);
    }

    public Optional<StateMachineRecord> get(String machineId) {
        return create
                .select()
                .from(STATE_MACHINE)
                .where(STATE_MACHINE.MACHINE_ID.eq(machineId))
                .fetchOptionalInto(StateMachineRecord.class);
    }

    @Override
    protected String getId(StateMachineRecord record) {
        return record.getMachineId();
    }

}

package com.innogrid.dao;

import com.innogrid.tables.records.StateMachineRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

import static com.innogrid.Tables.STATE_MACHINE;

@Repository
public class StateMachineDao {

    final DSLContext create;

    public StateMachineDao(DSLContext create) {
        this.create = create;
    }

    public Optional<StateMachineRecord> get(String machineId) {
        return create
                .select()
                .from(STATE_MACHINE)
                .where(STATE_MACHINE.MACHINE_ID.eq(machineId))
                .fetchOptionalInto(StateMachineRecord.class);
    }

    public StateMachineRecord insert(StateMachineRecord record) {
        return Objects.requireNonNull(create
                .insertInto(STATE_MACHINE)
                .set(record)
                .returning()
                .fetchOne()).into(StateMachineRecord.class);
    }
}

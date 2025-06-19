package com.innogrid.statemachine;

import com.innogrid.dao.StateMachineDao;
import com.innogrid.tables.records.StateMachineRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@AllArgsConstructor
public class StateMachineJdbcPersist implements StateMachinePersist<States, Events, String> {

    final StateMachineDao stateMachineDao;


    @Override
    public void write(StateMachineContext<States, Events> context, String machineId) throws Exception {
        log.debug("Persisting state machine {}", machineId);

        stateMachineDao.insert(new StateMachineRecord()
                .setMachineId(machineId)
                .setEvent(context.getEvent())
                .setExtendedState(context.getExtendedState().getVariables())
                .setState(context.getState())
                .setUpdatedAt(LocalDateTime.now())
        );
    }

    @Override
    public StateMachineContext<States, Events> read(String machineId) throws Exception {

        log.debug("Reading state machine {}", machineId);
        var record = stateMachineDao.get(machineId).orElseThrow();
        return new DefaultStateMachineContext<>(
                record.getState(),
                record.getEvent(),
                null,
                new DefaultExtendedState(record.getExtendedState()),
                null,
                record.getMachineId());
    }
}

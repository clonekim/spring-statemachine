package com.innogrid.statemachine;

import com.innogrid.dao.StateMachineDao;
import com.innogrid.tables.records.StateMachineRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.exception.NoDataFoundException;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class JdbcStateMachinePersist implements StateMachinePersist<States, Events, String> {

    final StateMachineDao stateMachineDao;

    @Override
    public void write(StateMachineContext<States, Events> context, String machineId) throws Exception {

        log.info("Persisting state machine {}", machineId);

        String state = context.getState().name();
        var record = new StateMachineRecord()
                .setState(state)
                .setMachineId(machineId);

        stateMachineDao.insert(record);
    }

    @Override
    public StateMachineContext<States, Events> read(String machineId) throws Exception {
        log.info("Reading state machine {}", machineId);

        var record = stateMachineDao.get(machineId).orElseThrow(NoDataFoundException::new);
        return new DefaultStateMachineContext<>(States.valueOf(record.getState()), null, null, null, null, record.getMachineId());
    }

}

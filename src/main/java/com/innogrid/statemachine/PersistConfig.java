package com.innogrid.statemachine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

@Configuration
public class PersistConfig {

    @Bean
    public StateMachinePersister<States, Events, String> persister(StateMachineJdbcPersist persist) {
        return new DefaultStateMachinePersister<>(persist);
    }

}
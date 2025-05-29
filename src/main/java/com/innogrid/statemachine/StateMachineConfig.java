package com.innogrid.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.EnumSet;

@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableStateMachineFactory
public class StateMachineConfig extends StateMachineConfigurerAdapter<States, Events> {


    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(new StateMachineEventListener());
    }


    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states
                .withStates()
                .initial(States.DRAFT)
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal()
                .source(States.DRAFT).target(States.DOC_SUBMITTED)
                .event(Events.SUBMIT)
                .guard(guard())
                .and()
                .withExternal()
                .source(States.DOC_SUBMITTED).target(States.DOC_UNDER_REVIEW).event(Events.REVIEW)
                .and()
                .withExternal()
                .source(States.DOC_UNDER_REVIEW).target(States.DOC_COMPLETED).event(Events.CONFIRM);

    }


    public Guard<States, Events> guard() {

        return context -> {

            log.info("StateMachineConfig.guard() ********* false");
            return false;
        };
    }

}

package com.innogrid.statemachine;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.util.IdGenerator;


@Slf4j
@Configuration
@EnableStateMachineFactory
@AllArgsConstructor
public class TicketStateMachineConfig extends StateMachineConfigurerAdapter<States, Events> {

    final IdGenerator idGenerator;
    final RequestActions requestActions;
    final RequestGuards requestGuards;

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {

        config
                .withConfiguration()
                .autoStartup(true)
                .machineId(idGenerator.generateId().toString())
                .listener(new TicketStateMachineEventListener());
    }


    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states
                .withStates()
                .initial(States.INITIAL)
                .end(States.END)
                .state(States.REQUEST)
                .state(States.DEVELOPMENT)
                .state(States.TERMINATE)

                // 요청 서브스테이트 설정
                .and()
                .withStates()
                .parent(States.REQUEST)
                .initial(States.REQUEST_PENDING_APPROVAL)
                .state(States.DEVELOPMENT)
                .state(States.TERMINATE)
                .and()

                .withStates()
                .parent(States.DEVELOPMENT)
                .initial(States.DEVELOPMENT_IN_PROGRESS)
                .state(States.DEVELOPMENT_COMPLETE)
                .state(States.TERMINATE)

                .and()
                .withStates()
                .parent(States.TEST)
                .choice(States.TEST)


        ;
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {

        transitions
                // 초기 상태에서 요청 생성
                .withExternal()
                .source(States.INITIAL).target(States.REQUEST)
                .guard(requestGuards.reviewerExists())
                .event(Events.CREATE_TICKET)
                .action(requestActions.createTicket())

                .and()
                .withInternal()
                .source(States.REQUEST_PENDING_APPROVAL)
                .event(Events.REQUEST_REJECTED)
                .action(requestActions.rejectAction())

                .and()
                .withInternal()
                .source(States.REQUEST_PENDING_APPROVAL)
                .event(Events.REQUEST_APPROVED)
                .action(requestActions.approveAction())

                .and()
                .withExternal()
                .source(States.REQUEST)
                .target(States.DEVELOPMENT)
                .event(Events.REQUEST_ALL_APPROVED)
                .guard(requestGuards.approvedAll())

                .and()
                .withExternal()
                .source(States.REQUEST)
                .target(States.TERMINATE)
                .event(Events.USER_CANCEL)
                .guard(userCancellable())
                .action(userCancel())

                .and()
                .withExternal()
                .source(States.DEVELOPMENT)

        ;
    }

    @Bean
    public Guard<States, Events> userCancellable() {
        return context ->  {
            log.debug("사용자가 취소 하려고 한다.\ntrue를 돌려주면 가능");
            return false;
        };
    }


    @Bean
    public Action<States, Events> userCancel() {
        return context -> {

        };
    }


}

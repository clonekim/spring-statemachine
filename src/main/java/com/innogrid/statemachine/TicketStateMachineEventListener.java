package com.innogrid.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@Slf4j
public class TicketStateMachineEventListener extends StateMachineListenerAdapter<States, Events> {

    @Override
    public void stateEntered(State<States, Events> state) {
        log.info("!State entered: {} {}", state.getId(), state.getStates());
    }


    @Override
    public void stateMachineStarted(StateMachine<States, Events> stateMachine) {
        log.info("!StateMachine Started: {}", stateMachine);
    }


    @Override
    public void stateMachineError(StateMachine<States, Events> stateMachine, Exception exception) {
        log.error("!StateMachine Error", exception);
    }

    @Override
    public void eventNotAccepted(Message<Events> event) {
        log.debug(event.toString());
        log.error("!Event Not Accepted");
    }
}

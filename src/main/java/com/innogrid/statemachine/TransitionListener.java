package com.innogrid.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.annotation.*;

@WithStateMachine
@Slf4j
public class TransitionListener {

    @OnTransition
    public void onTransition(StateContext<Object, Object> context) {
        log.info("-> onTransition {}", context.getEvent());
    }

    @OnTransitionStart
    public void onTransitionStart(StateContext<Object, Object> context) {
        log.info("-> onTransitionStart {}", context.getEvent());
    }

    @OnTransitionEnd
    public void onTransitionEnd(StateContext<Object, Object> context) {
        log.info("-> onTransitionEnd {}", context.getEvent());
    }


    @OnStateMachineError
    public void onStateMachineError(StateContext<Object, Object> context) {
        log.info("-> onStateMachineError {}", context.getEvent());
    }


    @OnEventNotAccepted
    public void onEventNotAccepted(StateContext<Object, Object> context) {
        log.info("-> onEventNotAccepted {}", context.getEvent() );
    }
}

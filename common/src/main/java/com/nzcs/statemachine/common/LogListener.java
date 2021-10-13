package com.nzcs.statemachine.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class LogListener extends StateMachineListenerAdapter<String, String> {

    @Override
    public void stateChanged(State<String, String> from, State<String, String> to) {
        super.stateChanged(from, to);
        log.info((from != null ? from.getId() : "") + " -> " + (to != null ? to.getId() : ""));
    }

    @Override
    public void transitionStarted(Transition<String, String> transition) {
        super.transitionStarted(transition);
        log.info("begin_transition: " + getEvent(transition));
    }

    @Override
    public void transitionEnded(Transition<String, String> transition) {
        super.transitionEnded(transition);
        log.info("end_transition: " + getEvent(transition));
    }


    private String getEvent(Transition<String, String> transition) {
        return transition != null
                ? (transition.getTrigger() != null ? transition.getTrigger().getEvent() : "Initialize sm")
                : "No transition";
    }
}

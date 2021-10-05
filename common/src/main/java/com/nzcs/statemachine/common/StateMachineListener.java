package com.nzcs.statemachine.common;

import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

@Component
public class StateMachineListener extends StateMachineListenerAdapter<String, String> {

    @Override
    public void stateChanged(State<String, String> from, State<String, String> to) {
        super.stateChanged(from, to);
        System.out.println((from != null ? from.getId() : "") + " -> " + (to != null ? to.getId() : ""));
    }

    @Override
    public void transitionStarted(Transition<String, String> transition) {
        super.transitionStarted(transition);
        System.out.println("begin_transition: " + transition.getName());
    }

    @Override
    public void transitionEnded(Transition<String, String> transition) {
        super.transitionEnded(transition);
        System.out.println("end_transition: " + transition.getName());
        System.out.println();
    }
}

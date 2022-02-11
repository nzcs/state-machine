package com.nzcs.statemachine.simple_choice;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

//@Component
public class ActionB implements Action<String, String> {

    @Override
    public void execute(StateContext<String, String> context) {
        System.out.println("pocs B: " + context.getStateMachine().getState());
    }
}

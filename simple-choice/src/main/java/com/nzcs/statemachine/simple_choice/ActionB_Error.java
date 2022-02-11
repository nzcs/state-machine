package com.nzcs.statemachine.simple_choice;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class ActionB_Error implements Action<String, String> {

    @Override
    public void execute(StateContext<String, String> context) {
        System.out.println("pocs error: " + context.getStateMachine().getState());
    }
}

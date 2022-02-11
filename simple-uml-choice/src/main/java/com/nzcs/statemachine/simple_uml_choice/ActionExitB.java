package com.nzcs.statemachine.simple_uml_choice;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;


@Component
public class ActionExitB implements Action<String, String> {

    @Override
    public void execute(StateContext<String, String> context) {
        System.out.println("exit B: " + context.getStateMachine().getState());
    }
}

package com.nzcs.statemachine.simple_choice;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class ActionB_Entry implements Action<String, String> {

    @Override
    public void execute(StateContext<String, String> context) {
        System.out.println("pocs entry: " + context.getStateMachine().getState());
//        context.getStateMachine().setStateMachineError(new RuntimeException());
//        throw new RuntimeException();
    }
}

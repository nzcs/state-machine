package com.nzcs.statemachine.aop;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class ActionC implements Action<String, String> {

    @Override
    public void execute(StateContext<String, String> context) {
        System.out.println("pocs ActionC: " + context.getStateMachine().getState());
    }
}
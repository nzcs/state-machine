package com.nzcs.statemachine.simple_choice;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GuardToB implements Guard<String, String> {

    @Override
    public boolean evaluate(StateContext<String, String> context) {
        Map<Object, Object> variables = context.getExtendedState().getVariables();
        return variables.get("X").equals("B");
    }
}

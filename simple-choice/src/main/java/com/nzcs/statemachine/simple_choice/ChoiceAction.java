package com.nzcs.statemachine.simple_choice;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ChoiceAction implements Action<String, String> {

    @Override
    public void execute(StateContext<String, String> context) {
        System.out.println("ChoiceAction: X = B");
        Map<Object, Object> variables = context.getExtendedState().getVariables();
        variables.put("X", "B");
    }
}

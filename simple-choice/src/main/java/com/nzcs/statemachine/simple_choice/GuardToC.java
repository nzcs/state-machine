package com.nzcs.statemachine.simple_choice;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class GuardToC implements Guard<String, String> {

    @Override
    public boolean evaluate(StateContext<String, String> context) {
        return false;
    }
}
